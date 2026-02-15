package com.banking.accounts.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * AccountWithCustomerDTO - Combined response DTO showing account details with minimal customer info.
 * Used in the extra endpoint for service-to-service communication.
 */
public class AccountWithCustomerDTO {

    private Long id;
    private String accountNumber;
    private String type;
    private BigDecimal balance;
    private LocalDateTime createdAt;
    private CustomerInfoDTO customerInfo;

    public AccountWithCustomerDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public CustomerInfoDTO getCustomerInfo() { return customerInfo; }
    public void setCustomerInfo(CustomerInfoDTO customerInfo) { this.customerInfo = customerInfo; }
}
