package com.banking.transactions.dao;

import com.banking.transactions.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Transaction Repository - DAO layer for Transaction entity.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Find all transactions for a specific account.
     */
    List<Transaction> findByAccountId(Long accountId);

    /**
     * Find transactions by status.
     */
    List<Transaction> findByStatus(String status);

    /**
     * Find transactions by account ID and status.
     */
    List<Transaction> findByAccountIdAndStatus(Long accountId, String status);
}
