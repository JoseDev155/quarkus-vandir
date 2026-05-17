package com.vandirstore.repository;

import com.vandirstore.model.User;
import com.vandirstore.model.enums.UserRole;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {
    
    public User findByEmail(String email) {
        return find("email", email).firstResult();
    }
    
    public List<User> listByRole(UserRole role) {
        return list("role", role);
    }
}
