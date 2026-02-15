package com.banking.customers.service;

import com.banking.customers.dao.CustomerRepository;
import com.banking.customers.dto.CustomerDTO;
import com.banking.customers.entity.Customer;
import com.banking.customers.exception.CustomerAlreadyExistsException;
import com.banking.customers.exception.ResourceNotFoundException;
import com.banking.customers.util.CustomerMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer sampleEntity;
    private CustomerDTO sampleDTO;

    @BeforeEach
    void setUp() {
        sampleEntity = new Customer();
        sampleEntity.setId(1L);
        sampleEntity.setFirstName("Jordan");
        sampleEntity.setLastName("Lee");
        sampleEntity.setEmail("jordan.lee@bank.com");
        sampleEntity.setPhone("+31 690000000");
        sampleEntity.setAddress("10 Bank Avenue, Rotterdam");
        sampleEntity.setCreatedAt(LocalDateTime.now());

        sampleDTO = new CustomerDTO();
        sampleDTO.setId(1L);
        sampleDTO.setFirstName("Jordan");
        sampleDTO.setLastName("Lee");
        sampleDTO.setEmail("jordan.lee@bank.com");
        sampleDTO.setPhone("+31 690000000");
        sampleDTO.setAddress("10 Bank Avenue, Rotterdam");
        sampleDTO.setCreatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Create customer - success")
    void testCreateCustomer_Success() {
        when(customerRepository.existsByEmail(anyString())).thenReturn(false);
        when(customerMapper.toEntity(any(CustomerDTO.class))).thenReturn(sampleEntity);
        when(customerRepository.save(any(Customer.class))).thenReturn(sampleEntity);
        when(customerMapper.toDTO(any(Customer.class))).thenReturn(sampleDTO);

        CustomerDTO result = customerService.createCustomer(sampleDTO);

        assertNotNull(result);
        assertEquals("Jordan", result.getFirstName());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    @DisplayName("Create customer - duplicate email")
    void testCreateCustomer_DuplicateEmail() {
        when(customerRepository.existsByEmail("jordan.lee@bank.com")).thenReturn(true);

        assertThrows(CustomerAlreadyExistsException.class, () -> customerService.createCustomer(sampleDTO));
    }

    @Test
    @DisplayName("Get customer by ID - found")
    void testGetCustomerById_Found() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(sampleEntity));
        when(customerMapper.toDTO(any(Customer.class))).thenReturn(sampleDTO);

        CustomerDTO result = customerService.getCustomerById(1L);

        assertNotNull(result);
        assertEquals("jordan.lee@bank.com", result.getEmail());
    }

    @Test
    @DisplayName("Get customer by ID - not found")
    void testGetCustomerById_NotFound() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> customerService.getCustomerById(99L));
    }

    @Test
    @DisplayName("Get all customers")
    void testGetAllCustomers() {
        when(customerRepository.findAll()).thenReturn(Arrays.asList(sampleEntity));
        when(customerMapper.toDTO(any(Customer.class))).thenReturn(sampleDTO);

        List<CustomerDTO> results = customerService.getAllCustomers();

        assertEquals(1, results.size());
    }

    @Test
    @DisplayName("Delete customer - success")
    void testDeleteCustomer_Success() {
        when(customerRepository.existsById(1L)).thenReturn(true);

        customerService.deleteCustomer(1L);

        verify(customerRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Delete customer - not found")
    void testDeleteCustomer_NotFound() {
        when(customerRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> customerService.deleteCustomer(99L));
    }
}
