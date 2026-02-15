package com.banking.transactions.controller;

import com.banking.transactions.dto.TransactionDTO;
import com.banking.transactions.service.TransactionService;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    private ObjectMapper objectMapper;
    private TransactionDTO sampleTransaction;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        sampleTransaction = new TransactionDTO();
        sampleTransaction.setId(1L);
        sampleTransaction.setAccountId(2002L);
        sampleTransaction.setType("Deposit");
        sampleTransaction.setAmount(new BigDecimal("750.00"));
        sampleTransaction.setTransactionDate(LocalDateTime.now());
        sampleTransaction.setStatus("SUCCESS");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("POST /api/transactions - Create transaction with valid data")
    void testCreateTransaction_ValidData() throws Exception {
        when(transactionService.createTransaction(any(TransactionDTO.class))).thenReturn(sampleTransaction);

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleTransaction)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.type").value("Deposit"))
                .andExpect(jsonPath("$.status").value("SUCCESS"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("GET /api/transactions/{id} - Get transaction by ID")
    void testGetTransactionById() throws Exception {
        when(transactionService.getTransactionById(1L)).thenReturn(sampleTransaction);

        mockMvc.perform(get("/api/transactions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("Deposit"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("GET /api/transactions - Get all transactions")
    void testGetAllTransactions() throws Exception {
        when(transactionService.getAllTransactions()).thenReturn(Arrays.asList(sampleTransaction));

        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("GET /api/transactions/account/{accountId} - Get by account ID")
    void testGetTransactionsByAccountId() throws Exception {
        when(transactionService.getTransactionsByAccountId(2002L)).thenReturn(Arrays.asList(sampleTransaction));

        mockMvc.perform(get("/api/transactions/account/2002"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("DELETE /api/transactions/{id} - Cancel transaction")
    void testDeleteTransaction() throws Exception {
        doNothing().when(transactionService).deleteTransaction(1L);

        mockMvc.perform(delete("/api/transactions/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("POST /api/transactions - Missing required fields should return 400")
    void testCreateTransaction_MissingFields() throws Exception {
        TransactionDTO invalid = new TransactionDTO();

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }
}
