package com.example.appointmentsystem.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.appointmentsystem.dto.CreateUserRequest;
import com.example.appointmentsystem.entity.User;
import com.example.appointmentsystem.enums.UserStatus;
import com.example.appointmentsystem.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public User createUser(CreateUserRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setMobilenumber(request.getMobilenumber());
        user.setStatus(UserStatus.ACTIVE);

        return userRepository.save(user);
    }
}
