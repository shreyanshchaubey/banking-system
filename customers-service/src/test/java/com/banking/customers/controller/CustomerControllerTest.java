package com.banking.customers.controller;

import com.banking.customers.dto.CustomerDTO;
import com.banking.customers.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    private ObjectMapper objectMapper;
    private CustomerDTO sampleCustomer;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        sampleCustomer = new CustomerDTO();
        sampleCustomer.setId(1L);
        sampleCustomer.setFirstName("Jordan");
        sampleCustomer.setLastName("Lee");
        sampleCustomer.setEmail("jordan.lee@bank.com");
        sampleCustomer.setPhone("+31 690000000");
        sampleCustomer.setAddress("10 Bank Avenue, Rotterdam");
        sampleCustomer.setCreatedAt(LocalDateTime.now());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("POST /api/customers - Create customer with valid data")
    void testCreateCustomer_ValidData() throws Exception {
        when(customerService.createCustomer(any(CustomerDTO.class))).thenReturn(sampleCustomer);

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleCustomer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Jordan"))
                .andExpect(jsonPath("$.email").value("jordan.lee@bank.com"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("POST /api/customers - Invalid email should return 400")
    void testCreateCustomer_InvalidEmail() throws Exception {
        sampleCustomer.setEmail("invalid-email");

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleCustomer)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("GET /api/customers/{id} - Get customer by ID")
    void testGetCustomerById() throws Exception {
        when(customerService.getCustomerById(1L)).thenReturn(sampleCustomer);

        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jordan"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("GET /api/customers - Get all customers")
    void testGetAllCustomers() throws Exception {
        when(customerService.getAllCustomers()).thenReturn(Arrays.asList(sampleCustomer));

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("PUT /api/customers/{id} - Update customer")
    void testUpdateCustomer() throws Exception {
        sampleCustomer.setPhone("+31 691111111");
        when(customerService.updateCustomer(eq(1L), any(CustomerDTO.class))).thenReturn(sampleCustomer);

        mockMvc.perform(put("/api/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleCustomer)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("DELETE /api/customers/{id} - Delete customer")
    void testDeleteCustomer() throws Exception {
        doNothing().when(customerService).deleteCustomer(1L);

        mockMvc.perform(delete("/api/customers/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("POST /api/customers - Missing required fields should return 400")
    void testCreateCustomer_MissingFields() throws Exception {
        CustomerDTO invalid = new CustomerDTO();

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }
}
