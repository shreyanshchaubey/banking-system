package com.banking.accounts.dto;

/**
 * CustomerInfoDTO - Minimal customer data exposed by Accounts Service.
 * Used for service-to-service communication to display only required customer details.
 * Full customer data is never exposed from the Accounts Service.
 */
public class CustomerInfoDTO {

    private Long customerId;
    private String firstName;
    private String lastName;
    private String email;

    public CustomerInfoDTO() {}

    public CustomerInfoDTO(Long customerId, String firstName, String lastName, String email) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
