package com.example.appointmentsystem.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentRequest {
    private Long doctorId;
    private Long slotId;
}
