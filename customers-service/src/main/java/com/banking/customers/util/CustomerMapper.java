package com.banking.customers.util;

import com.banking.customers.dto.CustomerDTO;
import com.banking.customers.entity.Customer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Customer Mapper - Utility for DTO ↔ Entity conversion using ModelMapper.
 */
@Component
public class CustomerMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public CustomerMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public CustomerDTO toDTO(Customer customer) {
        return modelMapper.map(customer, CustomerDTO.class);
    }

    public Customer toEntity(CustomerDTO customerDTO) {
        return modelMapper.map(customerDTO, Customer.class);
    }
}
