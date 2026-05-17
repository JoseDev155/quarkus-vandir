package com.vandirstore.repository;

import com.vandirstore.model.Customer;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CustomerRepository implements PanacheRepository<Customer> {
    
    public Customer findByName(String name) {
        return find("name", name).firstResult();
    }
    
    public Customer findByEmail(String email) {
        return find("email", email).firstResult();
    }
}
