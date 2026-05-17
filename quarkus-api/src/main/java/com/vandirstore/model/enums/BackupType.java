package com.vandirstore.model.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

public enum BackupType {
    AUTOMATIC("Automático"),
    MANUAL("Manual");

    private final String dbValue;

    BackupType(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }

    @Converter(autoApply = true)
    public static class Mapper implements AttributeConverter<BackupType, String> {
        @Override
        public String convertToDatabaseColumn(BackupType attribute) {
            return attribute == null ? null : attribute.getDbValue();
        }

        @Override
        public BackupType convertToEntityAttribute(String dbData) {
            if (dbData == null) return null;
            for (BackupType enumObj : BackupType.values()) {
                if (enumObj.getDbValue().equals(dbData)) return enumObj;
            }
            throw new IllegalArgumentException("Unknown database value: " + dbData);
        }
    }
}
