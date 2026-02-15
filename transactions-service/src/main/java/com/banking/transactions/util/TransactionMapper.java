package com.banking.transactions.util;

import com.banking.transactions.dto.TransactionDTO;
import com.banking.transactions.entity.Transaction;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public TransactionMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public TransactionDTO toDTO(Transaction transaction) {
        return modelMapper.map(transaction, TransactionDTO.class);
    }

    public Transaction toEntity(TransactionDTO transactionDTO) {
        return modelMapper.map(transactionDTO, Transaction.class);
    }
}
