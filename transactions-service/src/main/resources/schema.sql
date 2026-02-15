-- Transactions Service Database Schema
-- Table is created manually as per requirements (no entity auto-mapping)

CREATE TABLE IF NOT EXISTS transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_id BIGINT NOT NULL,
    type VARCHAR(50) NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    transaction_date TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL
);
