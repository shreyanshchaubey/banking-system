package com.banking.customers.dto;

/**
 * CustomerWithAccountsDTO - Combined DTO showing customer with their account summaries.
 * Used for the extra service-to-service communication endpoint.
 */
import java.util.List;

public class CustomerWithAccountsDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private List<AccountSummaryDTO> accounts;

    public CustomerWithAccountsDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public List<AccountSummaryDTO> getAccounts() { return accounts; }
    public void setAccounts(List<AccountSummaryDTO> accounts) { this.accounts = accounts; }
}
