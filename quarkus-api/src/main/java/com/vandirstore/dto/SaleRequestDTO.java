package com.vandirstore.dto;

import com.vandirstore.model.enums.PaymentMethod;
import java.util.List;

public class SaleRequestDTO {
    private Integer sellerId;
    private Integer customerId;
    private PaymentMethod paymentMethod;
    private List<SaleItemRequestDTO> items;

    public Integer getSellerId() { return sellerId; }
    public void setSellerId(Integer sellerId) { this.sellerId = sellerId; }
    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
    public List<SaleItemRequestDTO> getItems() { return items; }
    public void setItems(List<SaleItemRequestDTO> items) { this.items = items; }
}
