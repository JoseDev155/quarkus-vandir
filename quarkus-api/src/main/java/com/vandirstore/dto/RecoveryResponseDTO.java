package com.vandirstore.dto;

public class RecoveryResponseDTO {
    private String message;

    public RecoveryResponseDTO(String message) {
        this.message = message;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
