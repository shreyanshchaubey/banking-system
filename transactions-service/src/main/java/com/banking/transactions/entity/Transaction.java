package com.banking.transactions.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Transaction Entity - Maps to the 'transactions' table in the database.
 * Represents a banking transaction (deposit, withdrawal, transfer).
 * No JPA relationship mapping; account_id is stored as a plain column.
 */
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Column(name = "type", nullable = false, length = 50)
    private String type;

    @Column(name = "amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;

    @Column(name = "status", nullable = false, length = 50)
    private String status;

    public Transaction() {}

    public Transaction(Long id, Long accountId, String type, BigDecimal amount, LocalDateTime transactionDate, String status) {
        this.id = id;
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getAccountId() { return accountId; }
    public void setAccountId(Long accountId) { this.accountId = accountId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public LocalDateTime getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDateTime transactionDate) { this.transactionDate = transactionDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
