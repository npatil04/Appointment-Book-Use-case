package com.example.appointmentsystem.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.appointmentsystem.dto.CreateDoctorProfileRequest;
import com.example.appointmentsystem.entity.Doctor;
import com.example.appointmentsystem.entity.User;
import com.example.appointmentsystem.service.DoctorService;
import com.example.appointmentsystem.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/doctor/profile")
@RequiredArgsConstructor
public class DoctorProfileController {

    private final DoctorService doctorService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<Doctor> createProfile(
            Principal principal,
            @RequestBody CreateDoctorProfileRequest request) {

        User user = userService.getByUsername(principal.getName());
        Doctor doctor = doctorService.createDoctorProfile(user, request);

        return ResponseEntity.ok(doctor);
    }
}
