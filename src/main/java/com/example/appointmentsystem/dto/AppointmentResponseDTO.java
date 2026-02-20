package com.example.appointmentsystem.dto;

import java.time.LocalDateTime;

import com.example.appointmentsystem.enums.AppointmentStatus;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentResponseDTO {

    private Long appointmentId;

    private String doctorName;
    private String specialization;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private AppointmentStatus status;
}
