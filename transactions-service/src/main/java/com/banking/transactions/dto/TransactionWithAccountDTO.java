package com.banking.transactions.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * TransactionWithAccountDTO - Combined DTO for service-to-service communication.
 * Shows transaction with associated account info fetched from Accounts Service.
 */
public class TransactionWithAccountDTO {

    private Long id;
    private String type;
    private BigDecimal amount;
    private LocalDateTime transactionDate;
    private String status;
    private AccountInfoDTO accountInfo;

    public TransactionWithAccountDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public LocalDateTime getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDateTime transactionDate) { this.transactionDate = transactionDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public AccountInfoDTO getAccountInfo() { return accountInfo; }
    public void setAccountInfo(AccountInfoDTO accountInfo) { this.accountInfo = accountInfo; }
}
