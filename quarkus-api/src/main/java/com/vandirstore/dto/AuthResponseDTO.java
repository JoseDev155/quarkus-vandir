package com.vandirstore.dto;

public class AuthResponseDTO {
    private String token;
    private String rol;
    private String name;
    private Integer userId;

    public AuthResponseDTO(String token, String rol) {
        this.token = token;
        this.rol = rol;
        this.name = null;
        this.userId = null;
    }

    public AuthResponseDTO(String token, String rol, String name) {
        this.token = token;
        this.rol = rol;
        this.name = name;
        this.userId = null;
    }

    public AuthResponseDTO(String token, String rol, String name, Integer userId) {
        this.token = token;
        this.rol = rol;
        this.name = name;
        this.userId = userId;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
}
