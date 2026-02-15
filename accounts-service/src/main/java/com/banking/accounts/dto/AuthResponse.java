package com.banking.accounts.dto;

/**
 * AuthResponse DTO - Login response containing the JWT token.
 */
public class AuthResponse {

    private String token;

    public AuthResponse() {}

    public AuthResponse(String token) {
        this.token = token;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}
