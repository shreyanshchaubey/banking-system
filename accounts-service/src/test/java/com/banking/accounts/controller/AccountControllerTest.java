package com.banking.accounts.controller;

import com.banking.accounts.dto.AccountDTO;
import com.banking.accounts.service.AccountService;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for AccountController.
 * Tests all CRUD endpoints with valid and invalid data.
 */
@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    private ObjectMapper objectMapper;
    private AccountDTO sampleAccount;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        sampleAccount = new AccountDTO();
        sampleAccount.setId(1L);
        sampleAccount.setAccountNumber("NL91ABNA0417164300");
        sampleAccount.setCustomerId(1001L);
        sampleAccount.setType("Savings");
        sampleAccount.setBalance(new BigDecimal("5000.00"));
        sampleAccount.setCreatedAt(LocalDateTime.now());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("POST /api/accounts - Create account with valid data")
    void testCreateAccount_ValidData() throws Exception {
        when(accountService.createAccount(any(AccountDTO.class))).thenReturn(sampleAccount);

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleAccount)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountNumber").value("NL91ABNA0417164300"))
                .andExpect(jsonPath("$.type").value("Savings"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("POST /api/accounts - Create account with invalid data (missing fields)")
    void testCreateAccount_InvalidData() throws Exception {
        AccountDTO invalidAccount = new AccountDTO();
        // Missing required fields

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidAccount)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("GET /api/accounts/{id} - Get account by valid ID")
    void testGetAccountById() throws Exception {
        when(accountService.getAccountById(1L)).thenReturn(sampleAccount);

        mockMvc.perform(get("/api/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("NL91ABNA0417164300"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("GET /api/accounts - Get all accounts")
    void testGetAllAccounts() throws Exception {
        List<AccountDTO> accounts = Arrays.asList(sampleAccount);
        when(accountService.getAllAccounts()).thenReturn(accounts);

        mockMvc.perform(get("/api/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("PUT /api/accounts/{id} - Update account with valid data")
    void testUpdateAccount() throws Exception {
        sampleAccount.setBalance(new BigDecimal("10000.00"));
        when(accountService.updateAccount(eq(1L), any(AccountDTO.class))).thenReturn(sampleAccount);

        mockMvc.perform(put("/api/accounts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleAccount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(10000.00));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("DELETE /api/accounts/{id} - Delete account")
    void testDeleteAccount() throws Exception {
        doNothing().when(accountService).deleteAccount(1L);

        mockMvc.perform(delete("/api/accounts/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/accounts - Unauthenticated request should return 401/403")
    void testUnauthorizedAccess() throws Exception {
        mockMvc.perform(get("/api/accounts"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("POST /api/accounts - Negative balance should fail validation")
    void testCreateAccount_NegativeBalance() throws Exception {
        AccountDTO negativeBal = new AccountDTO();
        negativeBal.setAccountNumber("NL91ABNA0417164301");
        negativeBal.setCustomerId(1001L);
        negativeBal.setType("Savings");
        negativeBal.setBalance(new BigDecimal("-100.00"));

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(negativeBal)))
                .andExpect(status().isBadRequest());
    }
}
