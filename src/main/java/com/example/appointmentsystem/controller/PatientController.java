package com.example.appointmentsystem.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patient")
public class PatientController {

    @GetMapping("/ping")
    public String patientPing() {
        return "Patient access OK";
    }
}
