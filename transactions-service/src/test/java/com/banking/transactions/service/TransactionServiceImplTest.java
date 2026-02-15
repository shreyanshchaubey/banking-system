package com.banking.transactions.service;

import com.banking.transactions.dao.TransactionRepository;
import com.banking.transactions.dto.TransactionDTO;
import com.banking.transactions.entity.Transaction;
import com.banking.transactions.exception.InvalidTransactionException;
import com.banking.transactions.exception.ResourceNotFoundException;
import com.banking.transactions.util.TransactionMapper;
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

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Transaction sampleEntity;
    private TransactionDTO sampleDTO;

    @BeforeEach
    void setUp() {
        sampleEntity = new Transaction();
        sampleEntity.setId(1L);
        sampleEntity.setAccountId(2002L);
        sampleEntity.setType("Deposit");
        sampleEntity.setAmount(new BigDecimal("750.00"));
        sampleEntity.setTransactionDate(LocalDateTime.now());
        sampleEntity.setStatus("SUCCESS");

        sampleDTO = new TransactionDTO();
        sampleDTO.setId(1L);
        sampleDTO.setAccountId(2002L);
        sampleDTO.setType("Deposit");
        sampleDTO.setAmount(new BigDecimal("750.00"));
        sampleDTO.setTransactionDate(LocalDateTime.now());
        sampleDTO.setStatus("SUCCESS");
    }

    @Test
    @DisplayName("Create transaction - success")
    void testCreateTransaction_Success() {
        when(transactionMapper.toEntity(any(TransactionDTO.class))).thenReturn(sampleEntity);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(sampleEntity);
        when(transactionMapper.toDTO(any(Transaction.class))).thenReturn(sampleDTO);

        TransactionDTO result = transactionService.createTransaction(sampleDTO);

        assertNotNull(result);
        assertEquals("Deposit", result.getType());
        assertEquals("SUCCESS", result.getStatus());
    }

    @Test
    @DisplayName("Get transaction by ID - found")
    void testGetTransactionById_Found() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(sampleEntity));
        when(transactionMapper.toDTO(any(Transaction.class))).thenReturn(sampleDTO);

        TransactionDTO result = transactionService.getTransactionById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    @DisplayName("Get transaction by ID - not found")
    void testGetTransactionById_NotFound() {
        when(transactionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> transactionService.getTransactionById(99L));
    }

    @Test
    @DisplayName("Get transactions by account ID")
    void testGetTransactionsByAccountId() {
        when(transactionRepository.findByAccountId(2002L)).thenReturn(Arrays.asList(sampleEntity));
        when(transactionMapper.toDTO(any(Transaction.class))).thenReturn(sampleDTO);

        List<TransactionDTO> results = transactionService.getTransactionsByAccountId(2002L);

        assertEquals(1, results.size());
    }

    @Test
    @DisplayName("Cancel completed transaction should fail")
    void testDeleteTransaction_CompletedFails() {
        sampleEntity.setStatus("SUCCESS");
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(sampleEntity));

        assertThrows(InvalidTransactionException.class, () -> transactionService.deleteTransaction(1L));
    }

    @Test
    @DisplayName("Cancel pending transaction - success")
    void testDeleteTransaction_PendingSuccess() {
        sampleEntity.setStatus("PENDING");
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(sampleEntity));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(sampleEntity);

        transactionService.deleteTransaction(1L);

        assertEquals("CANCELLED", sampleEntity.getStatus());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    @DisplayName("Update transaction - only pending allowed")
    void testUpdateTransaction_CompletedFails() {
        sampleEntity.setStatus("SUCCESS");
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(sampleEntity));

        assertThrows(InvalidTransactionException.class, () -> transactionService.updateTransaction(1L, sampleDTO));
    }
}
