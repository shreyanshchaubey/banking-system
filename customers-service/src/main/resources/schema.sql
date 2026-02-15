-- Customers Service Database Schema
-- Table is created manually as per requirements (no entity auto-mapping)

CREATE TABLE IF NOT EXISTS customers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    address VARCHAR(250) NOT NULL,
    created_at TIMESTAMP NOT NULL
);
