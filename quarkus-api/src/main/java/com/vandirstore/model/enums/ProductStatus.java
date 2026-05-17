package com.vandirstore.model.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

public enum ProductStatus {
    ACTIVE("Activo"),
    INACTIVE("Inactivo");

    private final String dbValue;

    ProductStatus(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }

    @Converter(autoApply = true)
    public static class Mapper implements AttributeConverter<ProductStatus, String> {
        @Override
        public String convertToDatabaseColumn(ProductStatus attribute) {
            return attribute == null ? null : attribute.getDbValue();
        }

        @Override
        public ProductStatus convertToEntityAttribute(String dbData) {
            if (dbData == null) return null;
            for (ProductStatus enumObj : ProductStatus.values()) {
                if (enumObj.getDbValue().equals(dbData)) return enumObj;
            }
            throw new IllegalArgumentException("Unknown database value: " + dbData);
        }
    }
}
