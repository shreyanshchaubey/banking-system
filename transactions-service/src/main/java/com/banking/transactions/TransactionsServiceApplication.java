package com.banking.transactions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Transactions Service Application - Processes banking transactions.
 * Provides CRUD APIs for transaction creation, retrieval, update, and cancellation.
 * Runs independently on port 8083.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class TransactionsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransactionsServiceApplication.class, args);
    }
}
