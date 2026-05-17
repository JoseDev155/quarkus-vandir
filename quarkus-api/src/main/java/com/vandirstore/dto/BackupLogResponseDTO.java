package com.vandirstore.dto;

import com.vandirstore.model.enums.BackupType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BackupLogResponseDTO {
    private Integer id;
    private String fileName;
    private LocalDateTime generatedAt;
    private BigDecimal sizeMb;
    private BackupType type;
    private String generatedByName;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }
    public BigDecimal getSizeMb() { return sizeMb; }
    public void setSizeMb(BigDecimal sizeMb) { this.sizeMb = sizeMb; }
    public BackupType getType() { return type; }
    public void setType(BackupType type) { this.type = type; }
    public String getGeneratedByName() { return generatedByName; }
    public void setGeneratedByName(String generatedByName) { this.generatedByName = generatedByName; }
}
