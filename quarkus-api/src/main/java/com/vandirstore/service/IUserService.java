package com.vandirstore.service;

import com.vandirstore.dto.UserRequestDTO;
import com.vandirstore.dto.UserResponseDTO;
import com.vandirstore.model.User;
import com.vandirstore.model.enums.UserRole;
import java.util.List;

public interface IUserService {
    UserResponseDTO findById(Integer id);
    UserResponseDTO findByEmail(String email);
    User validateCredentials(String email, String password);
    List<UserResponseDTO> listAllUsers();
    List<UserResponseDTO> listByRole(UserRole role);
    UserResponseDTO createUser(UserRequestDTO userDTO);
    UserResponseDTO updateUser(Integer id, UserRequestDTO userDTO);
    boolean changeStatus(Integer id, boolean active);
    boolean deleteUser(Integer id);
}
