package com.vandirstore.repository;

import com.vandirstore.model.Product;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {
    
    public Product findByCode(String code) {
        return find("code", code).firstResult();
    }
    
    public List<Product> findLowStock() {
        return list("currentStock < minStock");
    }
    
    public List<Product> listByCategory(Integer categoryId) {
        return list("category.id", categoryId);
    }
}
