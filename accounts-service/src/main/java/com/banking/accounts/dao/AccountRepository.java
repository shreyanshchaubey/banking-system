package com.banking.accounts.dao;

import com.banking.accounts.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Account Repository - DAO layer for Account entity.
 * Provides database access operations for the accounts table.
 * All database operations go through this DAO layer.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * Find an account by its unique account number.
     */
    Optional<Account> findByAccountNumber(String accountNumber);

    /**
     * Find all accounts belonging to a specific customer.
     */
    List<Account> findByCustomerId(Long customerId);

    /**
     * Check if an account number already exists.
     */
    boolean existsByAccountNumber(String accountNumber);
}
