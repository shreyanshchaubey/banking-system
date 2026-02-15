-- Accounts Service Database Schema
-- Table is created manually as per requirements (no entity auto-mapping)

CREATE TABLE IF NOT EXISTS accounts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_number VARCHAR(20) NOT NULL UNIQUE,
    customer_id BIGINT NOT NULL,
    type VARCHAR(50) NOT NULL,
    balance DECIMAL(15,2) NOT NULL,
    created_at TIMESTAMP NOT NULL
);
