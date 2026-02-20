package com.example.appointmentsystem.dto;

import com.example.appointmentsystem.enums.AppointmentStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAppointmentStatusRequest {
    private AppointmentStatus status;
}
