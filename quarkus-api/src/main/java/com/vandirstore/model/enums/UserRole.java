package com.vandirstore.model.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

public enum UserRole {
    ADMINISTRATOR("Administrador"),
    MANAGER("Gerente"),
    SELLER("Vendedor");

    private final String dbValue;

    UserRole(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }

    @Converter(autoApply = true)
    public static class Mapper implements AttributeConverter<UserRole, String> {
        @Override
        public String convertToDatabaseColumn(UserRole attribute) {
            return attribute == null ? null : attribute.getDbValue();
        }

        @Override
        public UserRole convertToEntityAttribute(String dbData) {
            if (dbData == null) return null;
            for (UserRole enumObj : UserRole.values()) {
                if (enumObj.getDbValue().equals(dbData)) return enumObj;
            }
            throw new IllegalArgumentException("Unknown database value: " + dbData);
        }
    }
}
