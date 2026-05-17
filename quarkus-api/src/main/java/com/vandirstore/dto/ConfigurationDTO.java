package com.vandirstore.dto;

import java.math.BigDecimal;

public class ConfigurationDTO {
    private String companyName;
    private String contactPhone;
    private BigDecimal vatTax;
    private String currency;

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public BigDecimal getVatTax() { return vatTax; }
    public void setVatTax(BigDecimal vatTax) { this.vatTax = vatTax; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}
