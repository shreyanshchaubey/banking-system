package com.banking.accounts.service;

import com.banking.accounts.dao.AccountRepository;
import com.banking.accounts.dto.AccountDTO;
import com.banking.accounts.entity.Account;
import com.banking.accounts.exception.AccountAlreadyExistsException;
import com.banking.accounts.exception.ResourceNotFoundException;
import com.banking.accounts.util.AccountMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for AccountServiceImpl.
 * Tests business logic and validation in the service layer.
 */
@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Account sampleEntity;
    private AccountDTO sampleDTO;

    @BeforeEach
    void setUp() {
        sampleEntity = new Account();
        sampleEntity.setId(1L);
        sampleEntity.setAccountNumber("NL91ABNA0417164300");
        sampleEntity.setCustomerId(1001L);
        sampleEntity.setType("Savings");
        sampleEntity.setBalance(new BigDecimal("5000.00"));
        sampleEntity.setCreatedAt(LocalDateTime.now());

        sampleDTO = new AccountDTO();
        sampleDTO.setId(1L);
        sampleDTO.setAccountNumber("NL91ABNA0417164300");
        sampleDTO.setCustomerId(1001L);
        sampleDTO.setType("Savings");
        sampleDTO.setBalance(new BigDecimal("5000.00"));
        sampleDTO.setCreatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Create account - success")
    void testCreateAccount_Success() {
        when(accountRepository.existsByAccountNumber(anyString())).thenReturn(false);
        when(accountMapper.toEntity(any(AccountDTO.class))).thenReturn(sampleEntity);
        when(accountRepository.save(any(Account.class))).thenReturn(sampleEntity);
        when(accountMapper.toDTO(any(Account.class))).thenReturn(sampleDTO);

        AccountDTO result = accountService.createAccount(sampleDTO);

        assertNotNull(result);
        assertEquals("NL91ABNA0417164300", result.getAccountNumber());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    @DisplayName("Create account - duplicate account number")
    void testCreateAccount_DuplicateAccountNumber() {
        when(accountRepository.existsByAccountNumber("NL91ABNA0417164300")).thenReturn(true);

        assertThrows(AccountAlreadyExistsException.class, () -> accountService.createAccount(sampleDTO));
    }

    @Test
    @DisplayName("Get account by ID - found")
    void testGetAccountById_Found() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(sampleEntity));
        when(accountMapper.toDTO(any(Account.class))).thenReturn(sampleDTO);

        AccountDTO result = accountService.getAccountById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    @DisplayName("Get account by ID - not found")
    void testGetAccountById_NotFound() {
        when(accountRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> accountService.getAccountById(99L));
    }

    @Test
    @DisplayName("Get all accounts")
    void testGetAllAccounts() {
        when(accountRepository.findAll()).thenReturn(Arrays.asList(sampleEntity));
        when(accountMapper.toDTO(any(Account.class))).thenReturn(sampleDTO);

        List<AccountDTO> results = accountService.getAllAccounts();

        assertEquals(1, results.size());
    }

    @Test
    @DisplayName("Update account - success")
    void testUpdateAccount_Success() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(sampleEntity));
        when(accountRepository.save(any(Account.class))).thenReturn(sampleEntity);
        when(accountMapper.toDTO(any(Account.class))).thenReturn(sampleDTO);

        AccountDTO result = accountService.updateAccount(1L, sampleDTO);

        assertNotNull(result);
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    @DisplayName("Delete account - success")
    void testDeleteAccount_Success() {
        when(accountRepository.existsById(1L)).thenReturn(true);

        accountService.deleteAccount(1L);

        verify(accountRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Delete account - not found")
    void testDeleteAccount_NotFound() {
        when(accountRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> accountService.deleteAccount(99L));
    }
}
