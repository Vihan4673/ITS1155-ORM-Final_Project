package com.example.its1155ormfinal_project.bo.util;

import lk.ijse.supermarketfx.dto.CustomerDTO;
import lk.ijse.supermarketfx.entity.Customer;

/**
 * --------------------------------------------
 * Author: Shamodha Sahan
 * GitHub: https://github.com/shamodhas
 * Website: https://shamodha.com
 * --------------------------------------------
 * Created: 7/11/2025 2:13 PM
 * Project: Supermarket-layered
 * --------------------------------------------
 **/

// modal mapper
public class EntityDTOConverter {
    public CustomerDTO getCustomerDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setCustomerId(customer.getId());
        dto.setNic(customer.getNic());
        dto.setName(customer.getName());
        dto.setEmail(customer.getEmail());
        dto.setPhone(dto.getPhone());
        return dto;
    }

    public Customer getCustomer(CustomerDTO dto) {
        Customer customer = new Customer();
        customer.setId(dto.getCustomerId());
        customer.setNic(dto.getNic());
        customer.setName(dto.getName());
        customer.setEmail(dto.getEmail());
        customer.setPhone(dto.getPhone());
        return customer;
    }
}
