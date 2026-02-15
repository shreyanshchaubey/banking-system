package com.banking.customers.service;

import com.banking.customers.dto.CustomerDTO;
import com.banking.customers.dto.CustomerInfoDTO;
import com.banking.customers.dto.CustomerWithAccountsDTO;

import java.util.List;

/**
 * Customer Service Interface - Defines business logic contract for customer operations.
 */
public interface CustomerService {

    /**
     * Register a new customer.
     */
    CustomerDTO createCustomer(CustomerDTO customerDTO);

    /**
     * Retrieve customer by ID.
     */
    CustomerDTO getCustomerById(Long id);

    /**
     * Retrieve customer by email.
     */
    CustomerDTO getCustomerByEmail(String email);

    /**
     * Retrieve all customers.
     */
    List<CustomerDTO> getAllCustomers();

    /**
     * Update customer profile.
     */
    CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO);

    /**
     * Remove customer profile.
     */
    void deleteCustomer(Long id);

    /**
     * Get minimal customer info for service-to-service communication.
     * Returns only essential fields: ID, name, email.
     */
    CustomerInfoDTO getCustomerInfo(Long id);

    /**
     * Extra endpoint: Get customer with account summaries via service-to-service call.
     */
    CustomerWithAccountsDTO getCustomerWithAccounts(Long customerId);
}
