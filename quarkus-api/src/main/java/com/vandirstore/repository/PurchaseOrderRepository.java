package com.vandirstore.repository;

import com.vandirstore.model.PurchaseOrder;
import com.vandirstore.model.enums.PurchaseOrderStatus;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class PurchaseOrderRepository implements PanacheRepository<PurchaseOrder> {
    
    public List<PurchaseOrder> listByStatus(PurchaseOrderStatus status) {
        return list("status", status);
    }
    
    public List<PurchaseOrder> listByProvider(Integer providerId) {
        return list("provider.id", providerId);
    }
}
