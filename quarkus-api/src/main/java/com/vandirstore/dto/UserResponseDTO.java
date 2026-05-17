package com.vandirstore.dto;

import com.vandirstore.model.enums.UserRole;
import com.vandirstore.model.enums.UserStatus;

public class UserResponseDTO {
    private Integer id;
    private String name;
    private String email;
    private UserRole role;
    private UserStatus status;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }
}
