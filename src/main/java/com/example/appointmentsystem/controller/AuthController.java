package com.example.appointmentsystem.controller;

import com.example.appointmentsystem.entity.User;
import com.example.appointmentsystem.repository.UserRepository;
import com.example.appointmentsystem.security.JwtUtil;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.get("username"),
                        request.get("password")
                )
        );

        User user = userRepository.findByUsername(request.get("username"))
                .orElseThrow();

        String token = jwtUtil.generateToken(
                user.getUsername(),
                user.getRole().name()
        );

        return Map.of("token", token);
    }
}
