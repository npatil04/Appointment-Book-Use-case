package com.example.appointmentsystem.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.appointmentsystem.dto.UpdateAppointmentStatusRequest;
import com.example.appointmentsystem.service.AppointmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class DoctorAppointmentController {

	private final AppointmentService appointmentService;

	/**
	 * Doctor – View own appointment schedule
	 */
	@GetMapping("/doctor/appointments")
	public ResponseEntity<?> viewMySchedule(Principal principal) {
		return ResponseEntity.ok(appointmentService.getDoctorAppointments(principal.getName()));
	}

	@PutMapping("/doctor/{appointmentId}/status")
	public ResponseEntity<?> updateStatus( @PathVariable("appointmentId") Long appointmentId,
			@RequestBody UpdateAppointmentStatusRequest request, Principal principal) {
		return ResponseEntity.ok(
				appointmentService.updateAppointmentStatus(principal.getName(), appointmentId, request.getStatus()));
	}

}
