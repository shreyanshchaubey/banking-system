package com.banking.transactions.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Transaction DTO - Data Transfer Object for Transaction API requests and responses.
 * All validation is performed on the DTO layer.
 */
public class TransactionDTO {

    private Long id;

    @NotNull(message = "Account ID is required")
    @Positive(message = "Account ID must be a positive number")
    private Long accountId;

    @NotBlank(message = "Transaction type is required")
    @Size(max = 50, message = "Transaction type must not exceed 50 characters")
    private String type;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be positive")
    private BigDecimal amount;

    private LocalDateTime transactionDate;

    @NotBlank(message = "Status is required")
    @Size(max = 50, message = "Status must not exceed 50 characters")
    private String status;

    public TransactionDTO() {}

    public TransactionDTO(Long id, Long accountId, String type, BigDecimal amount, LocalDateTime transactionDate, String status) {
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
