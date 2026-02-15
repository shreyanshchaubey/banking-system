package com.banking.customers.dto;

import java.math.BigDecimal;

/**
 * AccountSummaryDTO - Minimal account data received from Accounts Service.
 * Used in service-to-service communication.
 */
public class AccountSummaryDTO {

    private String accountNumber;
    private String type;
    private BigDecimal balance;

    public AccountSummaryDTO() {}

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
}
