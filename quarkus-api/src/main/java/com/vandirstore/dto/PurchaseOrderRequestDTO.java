package com.vandirstore.dto;

import java.math.BigDecimal;

public class PurchaseOrderRequestDTO {
    private Integer providerId;
    private Integer managerId;
    private BigDecimal estimatedTotal;

    public Integer getProviderId() { return providerId; }
    public void setProviderId(Integer providerId) { this.providerId = providerId; }
    public Integer getManagerId() { return managerId; }
    public void setManagerId(Integer managerId) { this.managerId = managerId; }
    public BigDecimal getEstimatedTotal() { return estimatedTotal; }
    public void setEstimatedTotal(BigDecimal estimatedTotal) { this.estimatedTotal = estimatedTotal; }
}
