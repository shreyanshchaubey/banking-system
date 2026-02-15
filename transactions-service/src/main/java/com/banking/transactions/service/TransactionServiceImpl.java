package com.banking.transactions.service;

import com.banking.transactions.dao.TransactionRepository;
import com.banking.transactions.dto.AccountInfoDTO;
import com.banking.transactions.dto.TransactionDTO;
import com.banking.transactions.dto.TransactionWithAccountDTO;
import com.banking.transactions.entity.Transaction;
import com.banking.transactions.exception.InvalidTransactionException;
import com.banking.transactions.exception.ResourceNotFoundException;
import com.banking.transactions.util.TransactionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Transaction Service Implementation - Contains all business logic for transaction operations.
 * Uses RestTemplate for service-to-service calls to Accounts service.
 */
@Service
public class TransactionServiceImpl implements TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final RestTemplate restTemplate;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, TransactionMapper transactionMapper, RestTemplate restTemplate) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
        this.restTemplate = restTemplate;
    }

    /**
     * Initiates a new transaction.
     * Validates amount is positive, sets transaction date.
     */
    @Override
    public TransactionDTO createTransaction(TransactionDTO transactionDTO) {
        logger.info("Creating new transaction for account ID: {}", transactionDTO.getAccountId());

        if (transactionDTO.getAmount() != null && transactionDTO.getAmount().signum() <= 0) {
            throw new InvalidTransactionException("Transaction amount must be positive");
        }

        Transaction transaction = transactionMapper.toEntity(transactionDTO);
        transaction.setTransactionDate(LocalDateTime.now());

        if (transaction.getStatus() == null || transaction.getStatus().isBlank()) {
            transaction.setStatus("SUCCESS");
        }

        Transaction savedTransaction = transactionRepository.save(transaction);
        logger.info("Transaction created successfully with ID: {}", savedTransaction.getId());

        return transactionMapper.toDTO(savedTransaction);
    }

    /**
     * Retrieves transaction by ID.
     */
    @Override
    public TransactionDTO getTransactionById(Long id) {
        logger.debug("Fetching transaction with ID: {}", id);
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with ID: " + id));
        return transactionMapper.toDTO(transaction);
    }

    /**
     * Retrieves all transactions.
     */
    @Override
    public List<TransactionDTO> getAllTransactions() {
        logger.debug("Fetching all transactions");
        return transactionRepository.findAll().stream()
                .map(transactionMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves transaction history for a specific account.
     */
    @Override
    public List<TransactionDTO> getTransactionsByAccountId(Long accountId) {
        logger.debug("Fetching transactions for account ID: {}", accountId);
        return transactionRepository.findByAccountId(accountId).stream()
                .map(transactionMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves transactions filtered by status.
     */
    @Override
    public List<TransactionDTO> getTransactionsByStatus(String status) {
        logger.debug("Fetching transactions with status: {}", status);
        return transactionRepository.findByStatus(status).stream()
                .map(transactionMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Amends transaction details (only allowed if status is PENDING or SCHEDULED).
     */
    @Override
    public TransactionDTO updateTransaction(Long id, TransactionDTO transactionDTO) {
        logger.info("Updating transaction with ID: {}", id);

        Transaction existingTransaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with ID: " + id));

        // Only allow updates for PENDING or SCHEDULED transactions
        if (!"PENDING".equalsIgnoreCase(existingTransaction.getStatus())
                && !"SCHEDULED".equalsIgnoreCase(existingTransaction.getStatus())) {
            throw new InvalidTransactionException("Only PENDING or SCHEDULED transactions can be amended. Current status: " + existingTransaction.getStatus());
        }

        if (transactionDTO.getAmount() != null && transactionDTO.getAmount().signum() <= 0) {
            throw new InvalidTransactionException("Transaction amount must be positive");
        }

        existingTransaction.setAccountId(transactionDTO.getAccountId());
        existingTransaction.setType(transactionDTO.getType());
        existingTransaction.setAmount(transactionDTO.getAmount());
        existingTransaction.setStatus(transactionDTO.getStatus());

        Transaction updatedTransaction = transactionRepository.save(existingTransaction);
        logger.info("Transaction updated successfully with ID: {}", updatedTransaction.getId());

        return transactionMapper.toDTO(updatedTransaction);
    }

    /**
     * Cancels a scheduled transaction by setting its status to CANCELLED.
     * Only PENDING or SCHEDULED transactions can be cancelled.
     */
    @Override
    public void deleteTransaction(Long id) {
        logger.info("Cancelling transaction with ID: {}", id);

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with ID: " + id));

        if ("SUCCESS".equalsIgnoreCase(transaction.getStatus())) {
            throw new InvalidTransactionException("Completed transactions cannot be cancelled");
        }

        transaction.setStatus("CANCELLED");
        transactionRepository.save(transaction);
        logger.info("Transaction cancelled successfully with ID: {}", id);
    }

    /**
     * Extra endpoint: Retrieves transaction with account info.
     * Makes a RestTemplate call to Accounts Service.
     */
    @Override
    public TransactionWithAccountDTO getTransactionWithAccountInfo(Long transactionId) {
        logger.info("Fetching transaction with account info for transaction ID: {}", transactionId);

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with ID: " + transactionId));

        TransactionWithAccountDTO response = new TransactionWithAccountDTO();
        response.setId(transaction.getId());
        response.setType(transaction.getType());
        response.setAmount(transaction.getAmount());
        response.setTransactionDate(transaction.getTransactionDate());
        response.setStatus(transaction.getStatus());

        // Service-to-service call to Accounts Service
        try {
            AccountInfoDTO accountInfo = restTemplate.getForObject(
                    "http://localhost:8081/api/accounts/" + transaction.getAccountId(),
                    AccountInfoDTO.class
            );
            if (accountInfo != null) {
                accountInfo.setAccountId(transaction.getAccountId());
            }
            response.setAccountInfo(accountInfo);
        } catch (Exception e) {
            logger.warn("Unable to fetch account info for account ID: {}. Error: {}", transaction.getAccountId(), e.getMessage());
            AccountInfoDTO fallback = new AccountInfoDTO();
            fallback.setAccountId(transaction.getAccountId());
            response.setAccountInfo(fallback);
        }

        return response;
    }
}
