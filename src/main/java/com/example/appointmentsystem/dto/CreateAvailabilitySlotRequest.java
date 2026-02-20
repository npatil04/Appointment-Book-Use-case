package com.example.appointmentsystem.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CreateAvailabilitySlotRequest {

    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
