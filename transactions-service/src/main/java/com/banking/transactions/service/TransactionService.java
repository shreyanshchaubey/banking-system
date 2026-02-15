package com.banking.transactions.service;

import com.banking.transactions.dto.TransactionDTO;
import com.banking.transactions.dto.TransactionWithAccountDTO;

import java.util.List;

/**
 * Transaction Service Interface - Defines business logic contract for transaction operations.
 */
public interface TransactionService {

    /**
     * Initiate a new transaction.
     * Validates positive amount and sets transaction date.
     */
    TransactionDTO createTransaction(TransactionDTO transactionDTO);

    /**
     * Retrieve transaction by ID.
     */
    TransactionDTO getTransactionById(Long id);

    /**
     * Retrieve all transactions.
     */
    List<TransactionDTO> getAllTransactions();

    /**
     * Retrieve transaction history for a specific account.
     */
    List<TransactionDTO> getTransactionsByAccountId(Long accountId);

    /**
     * Retrieve transactions by status.
     */
    List<TransactionDTO> getTransactionsByStatus(String status);

    /**
     * Amend transaction details (only if status is PENDING).
     */
    TransactionDTO updateTransaction(Long id, TransactionDTO transactionDTO);

    /**
     * Cancel a scheduled transaction (set status to CANCELLED).
     */
    void deleteTransaction(Long id);

    /**
     * Extra endpoint: Get transaction with account info via service-to-service call.
     */
    TransactionWithAccountDTO getTransactionWithAccountInfo(Long transactionId);
}
