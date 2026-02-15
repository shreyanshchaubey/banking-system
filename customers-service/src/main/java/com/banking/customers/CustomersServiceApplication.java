package com.banking.customers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Customers Service Application - Manages customer profiles and registration.
 * Provides CRUD APIs for customer creation, retrieval, update, and deletion.
 * Runs independently on port 8082.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class CustomersServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomersServiceApplication.class, args);
    }
}
