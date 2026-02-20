package com.example.appointmentsystem.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/doctor")
public class DoctorController {

    @GetMapping("/ping")
    public String doctorPing() {
        return "Doctor access OK";
    }
}
