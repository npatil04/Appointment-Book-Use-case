package com.example.appointmentsystem.repository;

import com.example.appointmentsystem.entity.AvailabilitySlot;
import com.example.appointmentsystem.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AvailabilitySlotRepository extends JpaRepository<AvailabilitySlot, Long> {

    List<AvailabilitySlot> findByDoctor(Doctor doctor);
    
    List<AvailabilitySlot> findByDoctorId(Long doctorId);

    boolean existsByDoctorIdAndStartTimeLessThanAndEndTimeGreaterThan(
            Long doctorId,
            LocalDateTime endTime,
            LocalDateTime startTime
    );
}
