package com.banking.transactions.dto;

import java.math.BigDecimal;

/**
 * AccountInfoDTO - Minimal account data received from Accounts Service.
 */
public class AccountInfoDTO {

    private Long accountId;
    private String accountNumber;
    private String type;
    private BigDecimal balance;

    public AccountInfoDTO() {}

    public Long getAccountId() { return accountId; }
    public void setAccountId(Long accountId) { this.accountId = accountId; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
}
