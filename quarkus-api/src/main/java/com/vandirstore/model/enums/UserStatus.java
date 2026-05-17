package com.vandirstore.model.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

public enum UserStatus {
    ACTIVE("Activo"),
    BLOCKED("Bloqueado");

    private final String dbValue;

    UserStatus(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }

    @Converter(autoApply = true)
    public static class Mapper implements AttributeConverter<UserStatus, String> {
        @Override
        public String convertToDatabaseColumn(UserStatus attribute) {
            return attribute == null ? null : attribute.getDbValue();
        }

        @Override
        public UserStatus convertToEntityAttribute(String dbData) {
            if (dbData == null) return null;
            for (UserStatus enumObj : UserStatus.values()) {
                if (enumObj.getDbValue().equals(dbData)) return enumObj;
            }
            throw new IllegalArgumentException("Unknown database value: " + dbData);
        }
    }
}
