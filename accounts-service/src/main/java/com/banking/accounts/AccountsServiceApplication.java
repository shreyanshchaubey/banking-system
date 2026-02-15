package com.banking.accounts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Accounts Service Application - Manages bank account operations.
 * Provides CRUD APIs for account creation, retrieval, update, and deletion.
 * Runs independently on port 8081.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class AccountsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountsServiceApplication.class, args);
    }
}
