package com.example.appointmentsystem.repository;

import com.example.appointmentsystem.entity.Doctor;
import com.example.appointmentsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

	Optional<Doctor> findByUser(User user);

	boolean existsByLicenseNumber(String licenseNumber);

	Optional<Doctor> findByUserId(Long userId);
}
