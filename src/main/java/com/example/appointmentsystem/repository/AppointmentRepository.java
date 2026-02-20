package com.example.appointmentsystem.repository;

import com.example.appointmentsystem.entity.Appointment;
import com.example.appointmentsystem.entity.User;
import com.example.appointmentsystem.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Check if an appointment exists by id and appointment status
    boolean existsByIdAndAppointmentStatus(Long id, AppointmentStatus appointmentStatus);

    // Find all appointments for a patient (optional utility)
    List<Appointment> findByPatientId(Long patientId);

    // Find all appointments for a doctor (optional utility)
    List<Appointment> findByDoctorId(Long doctorId);
    
    // All appointments of a patient
    List<Appointment> findByPatient(User patient);

    // Optional: filter by status
    List<Appointment> findByPatientAndAppointmentStatus(
            User patient,
            AppointmentStatus appointmentStatus
    );
    
    Optional<Appointment> findBySlotId(Long slotId);
    
    List<Appointment> findByDoctorUserUsernameOrderBySlotStartTimeAsc(String username);


}
