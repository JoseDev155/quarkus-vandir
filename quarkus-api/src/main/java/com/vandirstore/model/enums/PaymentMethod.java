package com.vandirstore.model.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

public enum PaymentMethod {
    CASH("Efectivo"),
    CREDIT_CARD("Tarjeta de Crédito"),
    TRANSFER("Transferencia");

    private final String dbValue;

    PaymentMethod(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }

    @Converter(autoApply = true)
    public static class Mapper implements AttributeConverter<PaymentMethod, String> {
        @Override
        public String convertToDatabaseColumn(PaymentMethod attribute) {
            return attribute == null ? null : attribute.getDbValue();
        }

        @Override
        public PaymentMethod convertToEntityAttribute(String dbData) {
            if (dbData == null) return null;
            for (PaymentMethod enumObj : PaymentMethod.values()) {
                if (enumObj.getDbValue().equals(dbData)) return enumObj;
            }
            throw new IllegalArgumentException("Unknown database value: " + dbData);
        }
    }
}
