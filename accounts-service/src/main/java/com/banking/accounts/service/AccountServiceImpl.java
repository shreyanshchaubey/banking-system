package com.banking.accounts.service;

import com.banking.accounts.dao.AccountRepository;
import com.banking.accounts.dto.AccountDTO;
import com.banking.accounts.dto.AccountWithCustomerDTO;
import com.banking.accounts.dto.CustomerInfoDTO;
import com.banking.accounts.entity.Account;
import com.banking.accounts.exception.AccountAlreadyExistsException;
import com.banking.accounts.exception.ResourceNotFoundException;
import com.banking.accounts.util.AccountMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Account Service Implementation - Contains all business logic for account operations.
 * Controller delegates to this service; no business logic in controller.
 * Uses RestTemplate for service-to-service communication with Customers Service.
 */
@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final RestTemplate restTemplate;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, AccountMapper accountMapper, RestTemplate restTemplate) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.restTemplate = restTemplate;
    }

    /**
     * Creates a new bank account after validating account number uniqueness.
     * Sets the creation timestamp automatically.
     * Flow: Controller → Service → DAO → DB
     */
    @Override
    public AccountDTO createAccount(AccountDTO accountDTO) {
        logger.info("Creating new account with account number: {}", accountDTO.getAccountNumber());

        // Check if account number already exists
        if (accountRepository.existsByAccountNumber(accountDTO.getAccountNumber())) {
            throw new AccountAlreadyExistsException("Account with number " + accountDTO.getAccountNumber() + " already exists");
        }

        Account account = accountMapper.toEntity(accountDTO);
        account.setCreatedAt(LocalDateTime.now());

        Account savedAccount = accountRepository.save(account);
        logger.info("Account created successfully with ID: {}", savedAccount.getId());

        return accountMapper.toDTO(savedAccount);
    }

    /**
     * Retrieves account by its unique ID.
     * Throws ResourceNotFoundException if account does not exist.
     */
    @Override
    public AccountDTO getAccountById(Long id) {
        logger.debug("Fetching account with ID: {}", id);
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + id));
        return accountMapper.toDTO(account);
    }

    /**
     * Retrieves account by its unique account number.
     * Throws ResourceNotFoundException if account does not exist.
     */
    @Override
    public AccountDTO getAccountByAccountNumber(String accountNumber) {
        logger.debug("Fetching account with account number: {}", accountNumber);
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with number: " + accountNumber));
        return accountMapper.toDTO(account);
    }

    /**
     * Retrieves all accounts from the database.
     */
    @Override
    public List<AccountDTO> getAllAccounts() {
        logger.debug("Fetching all accounts");
        return accountRepository.findAll().stream()
                .map(accountMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all accounts belonging to a specific customer.
     */
    @Override
    public List<AccountDTO> getAccountsByCustomerId(Long customerId) {
        logger.debug("Fetching accounts for customer ID: {}", customerId);
        return accountRepository.findByCustomerId(customerId).stream()
                .map(accountMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Updates an existing account's details.
     * Account number uniqueness is validated if changed.
     * The creation timestamp is preserved.
     */
    @Override
    public AccountDTO updateAccount(Long id, AccountDTO accountDTO) {
        logger.info("Updating account with ID: {}", id);

        Account existingAccount = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + id));

        // Check uniqueness if account number is being changed
        if (!existingAccount.getAccountNumber().equals(accountDTO.getAccountNumber())
                && accountRepository.existsByAccountNumber(accountDTO.getAccountNumber())) {
            throw new AccountAlreadyExistsException("Account with number " + accountDTO.getAccountNumber() + " already exists");
        }

        existingAccount.setAccountNumber(accountDTO.getAccountNumber());
        existingAccount.setCustomerId(accountDTO.getCustomerId());
        existingAccount.setType(accountDTO.getType());
        existingAccount.setBalance(accountDTO.getBalance());

        Account updatedAccount = accountRepository.save(existingAccount);
        logger.info("Account updated successfully with ID: {}", updatedAccount.getId());

        return accountMapper.toDTO(updatedAccount);
    }

    /**
     * Deletes/closes an account by its ID.
     * Throws ResourceNotFoundException if account does not exist.
     */
    @Override
    public void deleteAccount(Long id) {
        logger.info("Deleting account with ID: {}", id);

        if (!accountRepository.existsById(id)) {
            throw new ResourceNotFoundException("Account not found with ID: " + id);
        }

        accountRepository.deleteById(id);
        logger.info("Account deleted successfully with ID: {}", id);
    }

    /**
     * Extra endpoint: Retrieves account details along with minimal customer information.
     * Makes a RestTemplate call to the Customers Service to fetch customer details.
     * Only minimal customer info (name, email) is exposed - not full customer data.
     */
    @Override
    public AccountWithCustomerDTO getAccountWithCustomerInfo(Long accountId) {
        logger.info("Fetching account with customer info for account ID: {}", accountId);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + accountId));

        AccountWithCustomerDTO response = new AccountWithCustomerDTO();
        response.setId(account.getId());
        response.setAccountNumber(account.getAccountNumber());
        response.setType(account.getType());
        response.setBalance(account.getBalance());
        response.setCreatedAt(account.getCreatedAt());

        // Service-to-service call using RestTemplate to fetch customer info
        try {
            CustomerInfoDTO customerInfo = restTemplate.getForObject(
                    "http://localhost:8082/api/customers/" + account.getCustomerId() + "/info",
                    CustomerInfoDTO.class
            );
            response.setCustomerInfo(customerInfo);
        } catch (Exception e) {
            logger.warn("Unable to fetch customer info for customer ID: {}. Error: {}", account.getCustomerId(), e.getMessage());
            // Return account data even if customer service is unavailable
            CustomerInfoDTO fallback = new CustomerInfoDTO();
            fallback.setCustomerId(account.getCustomerId());
            response.setCustomerInfo(fallback);
        }

        return response;
    }
}
