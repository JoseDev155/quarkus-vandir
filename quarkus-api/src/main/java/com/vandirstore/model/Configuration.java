package com.vandirstore.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "configuracion")
public class Configuration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre_empresa", nullable = false, length = 150)
    private String companyName;

    @Column(name = "telefono_contacto", length = 20)
    private String contactPhone;

    @Column(name = "impuesto_iva", precision = 5, scale = 2)
    private BigDecimal vatTax;

    @Column(name = "moneda", length = 10)
    private String currency;

    @Column(name = "ultima_modificacion", insertable = false, updatable = false)
    private LocalDateTime lastModified;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public BigDecimal getVatTax() {
        return vatTax;
    }

    public void setVatTax(BigDecimal vatTax) {
        this.vatTax = vatTax;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }
}
