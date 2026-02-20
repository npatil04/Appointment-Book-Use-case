package com.example.appointmentsystem.service;

import org.springframework.stereotype.Service;

import com.example.appointmentsystem.entity.User;
import com.example.appointmentsystem.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }
}
