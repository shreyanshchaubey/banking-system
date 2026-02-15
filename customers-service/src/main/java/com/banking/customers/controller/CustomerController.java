package com.banking.customers.controller;

import com.banking.customers.dto.CustomerDTO;
import com.banking.customers.dto.CustomerInfoDTO;
import com.banking.customers.dto.CustomerWithAccountsDTO;
import com.banking.customers.service.CustomerService;
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
 * Customer Controller - REST API endpoints for Customer operations.
 * This controller only delegates to the service layer; no business logic here.
 * All request/response use DTOs - entities are never directly exposed.
 */
@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customers", description = "Customer Management APIs for Banking System")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * POST /api/customers - Register a new customer.
     * Validates all fields via DTO validation annotations.
     * Returns 201 Created on success.
     */
    @PostMapping
    @Operation(summary = "Register a new customer", description = "Creates a new customer profile with validated data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Customer email already exists")
    })
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        CustomerDTO createdCustomer = customerService.createCustomer(customerDTO);
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

    /**
     * GET /api/customers/{id} - Retrieve customer by ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get customer by ID", description = "Retrieves customer profile by unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer found"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
        CustomerDTO customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    /**
     * GET /api/customers/email/{email} - Retrieve customer by email.
     */
    @GetMapping("/email/{email}")
    @Operation(summary = "Get customer by email", description = "Retrieves customer profile by email address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer found"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public ResponseEntity<CustomerDTO> getCustomerByEmail(@PathVariable String email) {
        CustomerDTO customer = customerService.getCustomerByEmail(email);
        return ResponseEntity.ok(customer);
    }

    /**
     * GET /api/customers - Retrieve all customers.
     */
    @GetMapping
    @Operation(summary = "Get all customers", description = "Retrieves a list of all customer profiles")
    @ApiResponse(responseCode = "200", description = "List of customers retrieved")
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<CustomerDTO> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    /**
     * PUT /api/customers/{id} - Update customer details.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update customer", description = "Updates an existing customer profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerDTO customerDTO) {
        CustomerDTO updatedCustomer = customerService.updateCustomer(id, customerDTO);
        return ResponseEntity.ok(updatedCustomer);
    }

    /**
     * DELETE /api/customers/{id} - Remove customer profile.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete customer", description = "Removes a customer profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public ResponseEntity<String> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok("Customer deleted successfully with ID: " + id);
    }

    /**
     * GET /api/customers/{id}/info - Minimal customer info for service-to-service calls.
     * Returns only essential fields (ID, name, email).
     * Used by Accounts Service to fetch customer details without full data exposure.
     */
    @GetMapping("/{id}/info")
    @Operation(summary = "Get minimal customer info", description = "Returns minimal customer data for inter-service communication")
    @ApiResponse(responseCode = "200", description = "Customer info retrieved")
    public ResponseEntity<CustomerInfoDTO> getCustomerInfo(@PathVariable Long id) {
        CustomerInfoDTO customerInfo = customerService.getCustomerInfo(id);
        return ResponseEntity.ok(customerInfo);
    }

    /**
     * GET /api/customers/{id}/with-accounts - Extra endpoint for service-to-service communication.
     * Fetches customer details along with account summaries from Accounts Service.
     */
    @GetMapping("/{id}/with-accounts")
    @Operation(summary = "Get customer with accounts", description = "Retrieves customer details with account summaries via service-to-service call")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer with accounts retrieved"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public ResponseEntity<CustomerWithAccountsDTO> getCustomerWithAccounts(@PathVariable Long id) {
        CustomerWithAccountsDTO customerWithAccounts = customerService.getCustomerWithAccounts(id);
        return ResponseEntity.ok(customerWithAccounts);
    }
}
