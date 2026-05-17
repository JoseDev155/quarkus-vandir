package com.vandirstore.service.impl;

import com.vandirstore.dto.UserRequestDTO;
import com.vandirstore.dto.UserResponseDTO;
import com.vandirstore.model.User;
import com.vandirstore.model.enums.UserRole;
import com.vandirstore.model.enums.UserStatus;
import com.vandirstore.repository.UserRepository;
import com.vandirstore.service.IUserService;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserServiceImpl implements IUserService {

    @Inject
    UserRepository userRepository;

    private UserResponseDTO toDTO(User user) {
        if (user == null) return null;
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setStatus(user.getStatus());
        return dto;
    }

    @Override
    public UserResponseDTO findById(Integer id) {
        return toDTO(userRepository.findById(id.longValue()));
    }

    @Override
    public UserResponseDTO findByEmail(String email) {
        return toDTO(userRepository.findByEmail(email));
    }

    @Override
    public User validateCredentials(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && BcryptUtil.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    @Override
    public List<UserResponseDTO> listAllUsers() {
        return userRepository.listAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDTO> listByRole(UserRole role) {
        return userRepository.listByRole(role).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserResponseDTO createUser(UserRequestDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        // Hash the password with Bcrypt before saving to DB
        user.setPassword(BcryptUtil.bcryptHash(userDTO.getPassword()));
        user.setRole(userDTO.getRole());
        user.setStatus(UserStatus.ACTIVE);
        
        userRepository.persist(user);
        return toDTO(user);
    }

    @Override
    @Transactional
    public UserResponseDTO updateUser(Integer id, UserRequestDTO userDTO) {
        User existingUser = userRepository.findById(id.longValue());
        if (existingUser != null) {
            existingUser.setName(userDTO.getName());
            existingUser.setEmail(userDTO.getEmail());
            existingUser.setRole(userDTO.getRole());
            // If the request contains a new password, hash it and update it
            if (userDTO.getPassword() != null && !userDTO.getPassword().trim().isEmpty()) {
                existingUser.setPassword(BcryptUtil.bcryptHash(userDTO.getPassword()));
            }
        }
        return toDTO(existingUser);
    }

    @Override
    @Transactional
    public boolean changeStatus(Integer id, boolean active) {
        User user = userRepository.findById(id.longValue());
        if (user != null) {
            user.setStatus(active ? UserStatus.ACTIVE : UserStatus.BLOCKED);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean deleteUser(Integer id) {
        return userRepository.deleteById(id.longValue());
    }
}
