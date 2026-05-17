package com.vandirstore.model;

import com.vandirstore.model.enums.BackupType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "respaldos_log")
public class BackupLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre_archivo", nullable = false, length = 255)
    private String fileName;

    @Column(name = "fecha_generacion", nullable = false)
    private LocalDateTime generatedAt;

    @Column(name = "tamano_mb", nullable = false, precision = 8, scale = 2)
    private BigDecimal sizeMb;

    @Column(name = "tipo")
    private BackupType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "generado_por")
    private User generatedBy;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public BigDecimal getSizeMb() {
        return sizeMb;
    }

    public void setSizeMb(BigDecimal sizeMb) {
        this.sizeMb = sizeMb;
    }

    public BackupType getType() {
        return type;
    }

    public void setType(BackupType type) {
        this.type = type;
    }

    public User getGeneratedBy() {
        return generatedBy;
    }

    public void setGeneratedBy(User generatedBy) {
        this.generatedBy = generatedBy;
    }
}
