package com.vandirstore.service;

import com.vandirstore.dto.CustomerDTO;
import java.util.List;

public interface ICustomerService {
    CustomerDTO findById(Integer id);
    CustomerDTO findByEmail(String email);
    List<CustomerDTO> listAllCustomers();
    CustomerDTO createCustomer(CustomerDTO customerDTO);
    CustomerDTO updateCustomer(Integer id, CustomerDTO customerDTO);
    boolean deleteCustomer(Integer id);
}
