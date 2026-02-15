package com.banking.transactions.controller;

import com.banking.transactions.dto.TransactionDTO;
import com.banking.transactions.dto.TransactionWithAccountDTO;
import com.banking.transactions.service.TransactionService;
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
 * Transaction Controller - REST API endpoints for Transaction operations.
 * Delegates all business logic to the service layer.
 */
@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transactions", description = "Transaction Management APIs for Banking System")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * POST /api/transactions - Initiate a new transaction.
     */
    @PostMapping
    @Operation(summary = "Create a new transaction", description = "Initiates a new banking transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transaction created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<TransactionDTO> createTransaction(@Valid @RequestBody TransactionDTO transactionDTO) {
        TransactionDTO createdTransaction = transactionService.createTransaction(transactionDTO);
        return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
    }

    /**
     * GET /api/transactions/{id} - Retrieve transaction by ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get transaction by ID", description = "Retrieves transaction details by unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction found"),
            @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    public ResponseEntity<TransactionDTO> getTransactionById(@PathVariable Long id) {
        TransactionDTO transaction = transactionService.getTransactionById(id);
        return ResponseEntity.ok(transaction);
    }

    /**
     * GET /api/transactions - Retrieve all transactions.
     */
    @GetMapping
    @Operation(summary = "Get all transactions", description = "Retrieves all banking transactions")
    @ApiResponse(responseCode = "200", description = "List of transactions retrieved")
    public ResponseEntity<List<TransactionDTO>> getAllTransactions() {
        List<TransactionDTO> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    /**
     * GET /api/transactions/account/{accountId} - Retrieve transactions by account.
     */
    @GetMapping("/account/{accountId}")
    @Operation(summary = "Get transactions by account ID", description = "Retrieves transaction history for a specific account")
    @ApiResponse(responseCode = "200", description = "Transaction history retrieved")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByAccountId(@PathVariable Long accountId) {
        List<TransactionDTO> transactions = transactionService.getTransactionsByAccountId(accountId);
        return ResponseEntity.ok(transactions);
    }

    /**
     * GET /api/transactions/status/{status} - Retrieve transactions by status.
     */
    @GetMapping("/status/{status}")
    @Operation(summary = "Get transactions by status", description = "Retrieves transactions filtered by status")
    @ApiResponse(responseCode = "200", description = "Filtered transactions retrieved")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByStatus(@PathVariable String status) {
        List<TransactionDTO> transactions = transactionService.getTransactionsByStatus(status);
        return ResponseEntity.ok(transactions);
    }

    /**
     * PUT /api/transactions/{id} - Amend transaction details.
     * Only PENDING or SCHEDULED transactions can be amended.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update transaction", description = "Amends transaction details (only for PENDING/SCHEDULED)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or transaction state"),
            @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    public ResponseEntity<TransactionDTO> updateTransaction(@PathVariable Long id, @Valid @RequestBody TransactionDTO transactionDTO) {
        TransactionDTO updatedTransaction = transactionService.updateTransaction(id, transactionDTO);
        return ResponseEntity.ok(updatedTransaction);
    }

    /**
     * DELETE /api/transactions/{id} - Cancel a scheduled transaction.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Cancel transaction", description = "Cancels a scheduled/pending transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction cancelled successfully"),
            @ApiResponse(responseCode = "400", description = "Completed transactions cannot be cancelled"),
            @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    public ResponseEntity<String> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.ok("Transaction cancelled successfully with ID: " + id);
    }

    /**
     * GET /api/transactions/{id}/with-account - Extra endpoint for service-to-service communication.
     * Fetches transaction details with associated account info from Accounts Service.
     */
    @GetMapping("/{id}/with-account")
    @Operation(summary = "Get transaction with account info", description = "Retrieves transaction with account details via service-to-service call")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction with account info retrieved"),
            @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    public ResponseEntity<TransactionWithAccountDTO> getTransactionWithAccountInfo(@PathVariable Long id) {
        TransactionWithAccountDTO transactionWithAccount = transactionService.getTransactionWithAccountInfo(id);
        return ResponseEntity.ok(transactionWithAccount);
    }
}
