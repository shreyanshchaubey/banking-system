package com.banking.accounts.util;

import com.banking.accounts.dto.AccountDTO;
import com.banking.accounts.entity.Account;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Account Mapper - Utility class for DTO ↔ Entity conversion.
 * Uses ModelMapper for mapping between AccountDTO and Account entity.
 * Ensures entities are never directly exposed to the controller layer.
 */
@Component
public class AccountMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public AccountMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     * Converts Account entity to AccountDTO.
     */
    public AccountDTO toDTO(Account account) {
        return modelMapper.map(account, AccountDTO.class);
    }

    /**
     * Converts AccountDTO to Account entity.
     */
    public Account toEntity(AccountDTO accountDTO) {
        return modelMapper.map(accountDTO, Account.class);
    }
}
