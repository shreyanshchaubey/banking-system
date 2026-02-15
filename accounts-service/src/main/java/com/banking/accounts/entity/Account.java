package com.banking.accounts.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Account Entity - Maps to the 'accounts' table in the database.
 * Represents a bank account with account number, type, balance, and customer reference.
 * No JPA relationship mapping is used; customer_id is stored as a simple column.
 */
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number", nullable = false, unique = true, length = 20)
    private String accountNumber;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "type", nullable = false, length = 50)
    private String type;

    @Column(name = "balance", nullable = false, precision = 15, scale = 2)
    private BigDecimal balance;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Account() {}

    public Account(Long id, String accountNumber, Long customerId, String type, BigDecimal balance, LocalDateTime createdAt) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.type = type;
        this.balance = balance;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
