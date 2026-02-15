package com.banking.accounts.exception;

/**
 * Custom exception thrown when attempting to create an account with a duplicate account number.
 */
public class AccountAlreadyExistsException extends RuntimeException {

    public AccountAlreadyExistsException(String message) {
        super(message);
    }
}
