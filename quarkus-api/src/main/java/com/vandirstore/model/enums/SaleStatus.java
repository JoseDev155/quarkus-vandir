package com.vandirstore.model.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

public enum SaleStatus {
    COMPLETED("Completado"),
    CANCELED("Anulado");

    private final String dbValue;

    SaleStatus(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }

    @Converter(autoApply = true)
    public static class Mapper implements AttributeConverter<SaleStatus, String> {
        @Override
        public String convertToDatabaseColumn(SaleStatus attribute) {
            return attribute == null ? null : attribute.getDbValue();
        }

        @Override
        public SaleStatus convertToEntityAttribute(String dbData) {
            if (dbData == null) return null;
            for (SaleStatus enumObj : SaleStatus.values()) {
                if (enumObj.getDbValue().equals(dbData)) return enumObj;
            }
            throw new IllegalArgumentException("Unknown database value: " + dbData);
        }
    }
}
