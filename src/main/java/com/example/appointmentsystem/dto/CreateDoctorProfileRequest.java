package com.example.appointmentsystem.dto;

import lombok.Data;

@Data
public class CreateDoctorProfileRequest {

    private String specialization;
    private String licenseNumber;
}
