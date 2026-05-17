package com.vandirstore.service.impl;

import com.vandirstore.dto.CustomerDTO;
import com.vandirstore.model.Customer;
import com.vandirstore.repository.CustomerRepository;
import com.vandirstore.service.ICustomerService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class CustomerServiceImpl implements ICustomerService {

    @Inject
    CustomerRepository customerRepository;

    private CustomerDTO toDTO(Customer customer) {
        if (customer == null) return null;
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setPhone(customer.getPhone());
        dto.setEmail(customer.getEmail());
        return dto;
    }

    @Override
    public CustomerDTO findById(Integer id) {
        return toDTO(customerRepository.findById(id.longValue()));
    }

    @Override
    public CustomerDTO findByEmail(String email) {
        return toDTO(customerRepository.findByEmail(email));
    }

    @Override
    public List<CustomerDTO> listAllCustomers() {
        return customerRepository.listAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setName(customerDTO.getName());
        customer.setPhone(customerDTO.getPhone());
        customer.setEmail(customerDTO.getEmail());
        
        customerRepository.persist(customer);
        return toDTO(customer);
    }

    @Override
    @Transactional
    public CustomerDTO updateCustomer(Integer id, CustomerDTO customerDTO) {
        Customer existingCustomer = customerRepository.findById(id.longValue());
        if (existingCustomer != null) {
            existingCustomer.setName(customerDTO.getName());
            existingCustomer.setPhone(customerDTO.getPhone());
            existingCustomer.setEmail(customerDTO.getEmail());
        }
        return toDTO(existingCustomer);
    }

    @Override
    @Transactional
    public boolean deleteCustomer(Integer id) {
        return customerRepository.deleteById(id.longValue());
    }
}
