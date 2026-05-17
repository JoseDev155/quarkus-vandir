package com.vandirstore.repository;

import com.vandirstore.model.SaleDetail;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class SaleDetailRepository implements PanacheRepository<SaleDetail> {
    
    public List<SaleDetail> listBySaleId(Integer saleId) {
        return list("sale.id", saleId);
    }
}
