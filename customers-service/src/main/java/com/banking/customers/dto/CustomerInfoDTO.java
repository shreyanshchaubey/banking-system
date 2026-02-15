package com.banking.customers.dto;

/**
 * CustomerInfoDTO - Minimal customer data for service-to-service communication.
 * Exposes only necessary fields (name, email) to other services.
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
