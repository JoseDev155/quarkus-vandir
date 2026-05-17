package com.vandirstore.model.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

public enum PurchaseOrderStatus {
    DRAFT("Borrador"),
    SENT("Enviada"),
    IN_TRANSIT("En Tránsito"),
    RECEIVED("Recibida"),
    CANCELED("Cancelada");

    private final String dbValue;

    PurchaseOrderStatus(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }

    @Converter(autoApply = true)
    public static class Mapper implements AttributeConverter<PurchaseOrderStatus, String> {
        @Override
        public String convertToDatabaseColumn(PurchaseOrderStatus attribute) {
            return attribute == null ? null : attribute.getDbValue();
        }

        @Override
        public PurchaseOrderStatus convertToEntityAttribute(String dbData) {
            if (dbData == null) return null;
            for (PurchaseOrderStatus enumObj : PurchaseOrderStatus.values()) {
                if (enumObj.getDbValue().equals(dbData)) return enumObj;
            }
            throw new IllegalArgumentException("Unknown database value: " + dbData);
        }
    }
}
