package com.example.appointmentsystem.service;

import org.springframework.stereotype.Service;

import com.example.appointmentsystem.dto.CreateDoctorProfileRequest;
import com.example.appointmentsystem.entity.Doctor;
import com.example.appointmentsystem.entity.User;
import com.example.appointmentsystem.enums.Role;
import com.example.appointmentsystem.repository.DoctorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public Doctor createDoctorProfile(User user, CreateDoctorProfileRequest request) {

        if (user.getRole() != Role.DOCTOR) {
            throw new RuntimeException("User is not a doctor");
        }

        if (doctorRepository.existsByLicenseNumber(request.getLicenseNumber())) {
            throw new RuntimeException("License number already exists");
        }

        Doctor doctor = new Doctor();
        doctor.setUser(user);
        doctor.setSpecialization(request.getSpecialization());
        doctor.setLicenseNumber(request.getLicenseNumber());

        return doctorRepository.save(doctor);
    }
    
    public Doctor getDoctorByUser(User user) {
        return doctorRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Doctor profile not found"));
    }

}
