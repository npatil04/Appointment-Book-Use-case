package com.example.appointmentsystem.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.appointmentsystem.dto.CreateAvailabilitySlotRequest;
import com.example.appointmentsystem.entity.AvailabilitySlot;
import com.example.appointmentsystem.entity.Doctor;
import com.example.appointmentsystem.entity.User;
import com.example.appointmentsystem.service.AvailabilitySlotService;
import com.example.appointmentsystem.service.DoctorService;
import com.example.appointmentsystem.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/doctor/availability")
@RequiredArgsConstructor
public class AvailabilitySlotController {

    private final AvailabilitySlotService slotService;
    private final DoctorService doctorService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<AvailabilitySlot> createSlot(
            Principal principal,
            @RequestBody CreateAvailabilitySlotRequest request) {

        User user = userService.getByUsername(principal.getName());
        System.out.println("User is :" +user );
        Doctor doctor = doctorService.getDoctorByUser(user);
        
        System.out.println("Doctor is :" +doctor );

        AvailabilitySlot slot = slotService.createSlot(doctor, request);
        
        System.out.println("Slot is inside contriller:" + slot);
        return ResponseEntity.ok(slot);
    }
}
