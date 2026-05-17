package com.vandirstore.dto;

import com.vandirstore.model.enums.PurchaseOrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PurchaseOrderResponseDTO {
    private Integer id;
    private String orderCode;
    private String providerName;
    private String managerName;
    private BigDecimal estimatedTotal;
    private PurchaseOrderStatus status;
    private LocalDateTime requestDate;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getOrderCode() { return orderCode; }
    public void setOrderCode(String orderCode) { this.orderCode = orderCode; }
    public String getProviderName() { return providerName; }
    public void setProviderName(String providerName) { this.providerName = providerName; }
    public String getManagerName() { return managerName; }
    public void setManagerName(String managerName) { this.managerName = managerName; }
    public BigDecimal getEstimatedTotal() { return estimatedTotal; }
    public void setEstimatedTotal(BigDecimal estimatedTotal) { this.estimatedTotal = estimatedTotal; }
    public PurchaseOrderStatus getStatus() { return status; }
    public void setStatus(PurchaseOrderStatus status) { this.status = status; }
    public LocalDateTime getRequestDate() { return requestDate; }
    public void setRequestDate(LocalDateTime requestDate) { this.requestDate = requestDate; }
}
