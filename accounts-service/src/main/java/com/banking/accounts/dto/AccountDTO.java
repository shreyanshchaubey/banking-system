package com.banking.accounts.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Account DTO - Data Transfer Object for Account API requests and responses.
 * All validation is performed on the DTO layer, not on the entity.
 * Used for transferring data between controller and service layers.
 */
public class AccountDTO {

    private Long id;

    @NotBlank(message = "Account number is required")
    @Size(min = 5, max = 20, message = "Account number must be between 5 and 20 characters")
    private String accountNumber;

    @NotNull(message = "Customer ID is required")
    @Positive(message = "Customer ID must be a positive number")
    private Long customerId;

    @NotBlank(message = "Account type is required")
    @Size(max = 50, message = "Account type must not exceed 50 characters")
    private String type;

    @NotNull(message = "Balance is required")
    @DecimalMin(value = "0.00", message = "Balance cannot be negative")
    private BigDecimal balance;

    private LocalDateTime createdAt;

    public AccountDTO() {}

    public AccountDTO(Long id, String accountNumber, Long customerId, String type, BigDecimal balance, LocalDateTime createdAt) {
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
