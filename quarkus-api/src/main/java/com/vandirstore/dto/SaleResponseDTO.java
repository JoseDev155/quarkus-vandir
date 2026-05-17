package com.vandirstore.dto;

import com.vandirstore.model.enums.SaleStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SaleResponseDTO {
    private Integer id;
    private String ticketCode;
    private String customerName;
    private BigDecimal subtotal;
    private BigDecimal totalVat;
    private BigDecimal totalSale;
    private SaleStatus status;
    private LocalDateTime saleDate;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTicketCode() { return ticketCode; }
    public void setTicketCode(String ticketCode) { this.ticketCode = ticketCode; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    public BigDecimal getTotalVat() { return totalVat; }
    public void setTotalVat(BigDecimal totalVat) { this.totalVat = totalVat; }
    public BigDecimal getTotalSale() { return totalSale; }
    public void setTotalSale(BigDecimal totalSale) { this.totalSale = totalSale; }
    public SaleStatus getStatus() { return status; }
    public void setStatus(SaleStatus status) { this.status = status; }
    public LocalDateTime getSaleDate() { return saleDate; }
    public void setSaleDate(LocalDateTime saleDate) { this.saleDate = saleDate; }
}
