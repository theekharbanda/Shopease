package com.example.shopease.services;

import com.example.shopease.dtos.requests.UserReqDTO;
import com.example.shopease.entities.User;
import com.example.shopease.repos.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(UserReqDTO userReqDTO) {
        if(userRepo.findByEmail(userReqDTO.getEmail()).isPresent()) {
            throw new IllegalStateException("User with email " + userReqDTO.getEmail() + " already exists");
        }
        User userToBeSaved = User.builder()
                .email(userReqDTO.getEmail())
                .password(passwordEncoder.encode(userReqDTO.getPassword()))
                .name(userReqDTO.getName())
                .role(User.Role.USER)
                .build();
        return userRepo.save(userToBeSaved);
    }

    public User registerAdmin(UserReqDTO userReqDTO) {
        if(userRepo.findByEmail(userReqDTO.getEmail()).isPresent()) {
            throw new IllegalStateException("Admin with email " + userReqDTO.getEmail() + " already exists");
        }
        User user = User.builder()
                .email(userReqDTO.getEmail())
                .password(passwordEncoder.encode(userReqDTO.getPassword()))
                .name(userReqDTO.getName())
                .role(User.Role.ADMIN)
                .build();
        return userRepo.save(user);
    }

    public User getUserById(Long userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }
}
