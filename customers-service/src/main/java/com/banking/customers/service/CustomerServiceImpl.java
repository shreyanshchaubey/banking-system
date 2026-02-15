package com.banking.customers.service;

import com.banking.customers.dao.CustomerRepository;
import com.banking.customers.dto.*;
import com.banking.customers.entity.Customer;
import com.banking.customers.exception.CustomerAlreadyExistsException;
import com.banking.customers.exception.ResourceNotFoundException;
import com.banking.customers.util.CustomerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Customer Service Implementation - Contains all business logic for customer operations.
 * Controller delegates to this service; no business logic in controller.
 * Uses RestTemplate for service-to-service calls to Accounts Service.
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final RestTemplate restTemplate;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper, RestTemplate restTemplate) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.restTemplate = restTemplate;
    }

    /**
     * Registers a new customer after validating email uniqueness.
     * Sets creation timestamp automatically.
     */
    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        logger.info("Registering new customer with email: {}", customerDTO.getEmail());

        if (customerRepository.existsByEmail(customerDTO.getEmail())) {
            throw new CustomerAlreadyExistsException("Customer with email " + customerDTO.getEmail() + " already exists");
        }

        Customer customer = customerMapper.toEntity(customerDTO);
        customer.setCreatedAt(LocalDateTime.now());

        Customer savedCustomer = customerRepository.save(customer);
        logger.info("Customer registered successfully with ID: {}", savedCustomer.getId());

        return customerMapper.toDTO(savedCustomer);
    }

    /**
     * Retrieves customer by unique ID.
     */
    @Override
    public CustomerDTO getCustomerById(Long id) {
        logger.debug("Fetching customer with ID: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));
        return customerMapper.toDTO(customer);
    }

    /**
     * Retrieves customer by email address.
     */
    @Override
    public CustomerDTO getCustomerByEmail(String email) {
        logger.debug("Fetching customer with email: {}", email);
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with email: " + email));
        return customerMapper.toDTO(customer);
    }

    /**
     * Retrieves all customers from the database.
     */
    @Override
    public List<CustomerDTO> getAllCustomers() {
        logger.debug("Fetching all customers");
        return customerRepository.findAll().stream()
                .map(customerMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Updates customer profile.
     * Email uniqueness is validated if changed.
     * Creation timestamp is preserved.
     */
    @Override
    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
        logger.info("Updating customer with ID: {}", id);

        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));

        // Check email uniqueness if changed
        if (!existingCustomer.getEmail().equals(customerDTO.getEmail())
                && customerRepository.existsByEmail(customerDTO.getEmail())) {
            throw new CustomerAlreadyExistsException("Customer with email " + customerDTO.getEmail() + " already exists");
        }

        existingCustomer.setFirstName(customerDTO.getFirstName());
        existingCustomer.setLastName(customerDTO.getLastName());
        existingCustomer.setEmail(customerDTO.getEmail());
        existingCustomer.setPhone(customerDTO.getPhone());
        existingCustomer.setAddress(customerDTO.getAddress());

        Customer updatedCustomer = customerRepository.save(existingCustomer);
        logger.info("Customer updated successfully with ID: {}", updatedCustomer.getId());

        return customerMapper.toDTO(updatedCustomer);
    }

    /**
     * Removes a customer profile by ID.
     */
    @Override
    public void deleteCustomer(Long id) {
        logger.info("Deleting customer with ID: {}", id);

        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found with ID: " + id);
        }

        customerRepository.deleteById(id);
        logger.info("Customer deleted successfully with ID: {}", id);
    }

    /**
     * Returns minimal customer info for service-to-service communication.
     * Only exposes ID, first name, last name, and email.
     */
    @Override
    public CustomerInfoDTO getCustomerInfo(Long id) {
        logger.debug("Fetching minimal customer info for ID: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));

        return new CustomerInfoDTO(customer.getId(), customer.getFirstName(), customer.getLastName(), customer.getEmail());
    }

    /**
     * Extra endpoint: Fetches customer details with account summaries.
     * Makes a RestTemplate call to Accounts Service to get customer's accounts.
     */
    @Override
    public CustomerWithAccountsDTO getCustomerWithAccounts(Long customerId) {
        logger.info("Fetching customer with accounts for customer ID: {}", customerId);

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + customerId));

        CustomerWithAccountsDTO response = new CustomerWithAccountsDTO();
        response.setId(customer.getId());
        response.setFirstName(customer.getFirstName());
        response.setLastName(customer.getLastName());
        response.setEmail(customer.getEmail());

        // Service-to-service call to Accounts Service
        try {
            ResponseEntity<List<AccountSummaryDTO>> accountsResponse = restTemplate.exchange(
                    "http://localhost:8081/api/accounts/customer/" + customerId,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<AccountSummaryDTO>>() {}
            );
            response.setAccounts(accountsResponse.getBody());
        } catch (Exception e) {
            logger.warn("Unable to fetch accounts for customer ID: {}. Error: {}", customerId, e.getMessage());
            response.setAccounts(Collections.emptyList());
        }

        return response;
    }
}
