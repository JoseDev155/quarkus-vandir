package com.vandirstore.repository;

import com.vandirstore.model.Sale;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class SaleRepository implements PanacheRepository<Sale> {
    
    public Sale findByTicketCode(String code) {
        return find("ticketCode", code).firstResult();
    }
    
    public List<Sale> listByDateRange(LocalDateTime start, LocalDateTime end) {
        return list("saleDate >= ?1 and saleDate <= ?2", start, end);
    }
}
