package com.example.appointmentsystem.entity;

import com.example.appointmentsystem.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Patient is a User with role = PATIENT
     */
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    /**
     * Doctor for the appointment
     */
    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    /**
     * One appointment maps to one availability slot
     */
    @OneToOne
    @JoinColumn(name = "slot_id", nullable = false, unique = true)
    private AvailabilitySlot slot;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus appointmentStatus;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.appointmentStatus == null) {
            this.appointmentStatus = AppointmentStatus.BOOKED;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
