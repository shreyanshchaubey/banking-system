package com.banking.accounts.service;

import com.banking.accounts.dto.AccountDTO;
import com.banking.accounts.dto.AccountWithCustomerDTO;

import java.util.List;

/**
 * Account Service Interface - Defines business logic contract for account operations.
 * All business logic resides in the service layer implementation.
 */
public interface AccountService {

    /**
     * Create a new bank account.
     * Validates uniqueness of account number and sets creation timestamp.
     */
    AccountDTO createAccount(AccountDTO accountDTO);

    /**
     * Retrieve account details by account ID.
     */
    AccountDTO getAccountById(Long id);

    /**
     * Retrieve account details by account number.
     */
    AccountDTO getAccountByAccountNumber(String accountNumber);

    /**
     * Retrieve all accounts in the system.
     */
    List<AccountDTO> getAllAccounts();

    /**
     * Retrieve all accounts for a specific customer.
     */
    List<AccountDTO> getAccountsByCustomerId(Long customerId);

    /**
     * Update existing account information.
     * Account number cannot be changed after creation.
     */
    AccountDTO updateAccount(Long id, AccountDTO accountDTO);

    /**
     * Delete/close an account by ID.
     */
    void deleteAccount(Long id);

    /**
     * Extra endpoint: Get account with minimal customer info via service-to-service call.
     * Calls Customers Service using RestTemplate to fetch customer details.
     */
    AccountWithCustomerDTO getAccountWithCustomerInfo(Long accountId);
}
