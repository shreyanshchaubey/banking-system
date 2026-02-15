package com.banking.customers.dao;

import com.banking.customers.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Customer Repository - DAO layer for Customer entity.
 * Provides database access operations for the customers table.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * Find a customer by email address.
     */
    Optional<Customer> findByEmail(String email);

    /**
     * Check if a customer with the given email already exists.
     */
    boolean existsByEmail(String email);
}
