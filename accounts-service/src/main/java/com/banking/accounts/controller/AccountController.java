package com.banking.accounts.controller;

import com.banking.accounts.dto.AccountDTO;
import com.banking.accounts.dto.AccountWithCustomerDTO;
import com.banking.accounts.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Account Controller - REST API endpoints for Account operations.
 * This controller only delegates to the service layer; no business logic here.
 * All request/response use DTOs - entities are never directly exposed.
 */
@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Accounts", description = "Account Management APIs for Banking System")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * POST /api/accounts - Create a new bank account.
     * Validates all fields via DTO validation annotations.
     * Returns 201 Created on success.
     */
    @PostMapping
    @Operation(summary = "Create a new account", description = "Opens a new bank account with validated details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Account created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Account number already exists")
    })
    public ResponseEntity<AccountDTO> createAccount(@Valid @RequestBody AccountDTO accountDTO) {
        AccountDTO createdAccount = accountService.createAccount(accountDTO);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

    /**
     * GET /api/accounts/{id} - Retrieve account by ID.
     * Returns 200 OK with account details.
     * Returns 404 Not Found if account does not exist.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get account by ID", description = "Retrieves account details by its unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account found"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable Long id) {
        AccountDTO account = accountService.getAccountById(id);
        return ResponseEntity.ok(account);
    }

    /**
     * GET /api/accounts/number/{accountNumber} - Retrieve account by account number.
     * Returns 200 OK with account details.
     */
    @GetMapping("/number/{accountNumber}")
    @Operation(summary = "Get account by account number", description = "Retrieves account details by its unique account number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account found"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    public ResponseEntity<AccountDTO> getAccountByAccountNumber(@PathVariable String accountNumber) {
        AccountDTO account = accountService.getAccountByAccountNumber(accountNumber);
        return ResponseEntity.ok(account);
    }

    /**
     * GET /api/accounts - Retrieve all accounts.
     * Returns 200 OK with list of all accounts.
     */
    @GetMapping
    @Operation(summary = "Get all accounts", description = "Retrieves a list of all bank accounts")
    @ApiResponse(responseCode = "200", description = "List of accounts retrieved")
    public ResponseEntity<List<AccountDTO>> getAllAccounts() {
        List<AccountDTO> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    /**
     * GET /api/accounts/customer/{customerId} - Retrieve accounts by customer ID.
     * Returns all accounts belonging to a specific customer.
     */
    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get accounts by customer ID", description = "Retrieves all accounts for a specific customer")
    @ApiResponse(responseCode = "200", description = "List of customer accounts retrieved")
    public ResponseEntity<List<AccountDTO>> getAccountsByCustomerId(@PathVariable Long customerId) {
        List<AccountDTO> accounts = accountService.getAccountsByCustomerId(customerId);
        return ResponseEntity.ok(accounts);
    }

    /**
     * PUT /api/accounts/{id} - Update account details.
     * Returns 200 OK with updated account.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update account", description = "Updates an existing bank account's information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Account not found"),
            @ApiResponse(responseCode = "409", description = "Account number already exists")
    })
    public ResponseEntity<AccountDTO> updateAccount(@PathVariable Long id, @Valid @RequestBody AccountDTO accountDTO) {
        AccountDTO updatedAccount = accountService.updateAccount(id, accountDTO);
        return ResponseEntity.ok(updatedAccount);
    }

    /**
     * DELETE /api/accounts/{id} - Delete/close an account.
     * Returns 200 OK with confirmation message.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete account", description = "Closes/removes a bank account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    public ResponseEntity<String> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.ok("Account deleted successfully with ID: " + id);
    }

    /**
     * GET /api/accounts/{id}/with-customer - Extra endpoint for service-to-service communication.
     * Fetches account details along with minimal customer information from Customers Service.
     * Only exposes minimal customer data (name, email) - not full customer profile.
     */
    @GetMapping("/{id}/with-customer")
    @Operation(summary = "Get account with customer info", description = "Retrieves account details with minimal customer info via service-to-service call")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account with customer info retrieved"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    public ResponseEntity<AccountWithCustomerDTO> getAccountWithCustomerInfo(@PathVariable Long id) {
        AccountWithCustomerDTO accountWithCustomer = accountService.getAccountWithCustomerInfo(id);
        return ResponseEntity.ok(accountWithCustomer);
    }
}
