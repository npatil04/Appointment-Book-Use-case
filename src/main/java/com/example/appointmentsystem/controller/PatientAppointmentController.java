package com.example.appointmentsystem.controller;

import com.example.appointmentsystem.dto.AppointmentRequest;
import com.example.appointmentsystem.dto.AppointmentResponseDTO;
import com.example.appointmentsystem.dto.RescheduleAppointmentRequest;
import com.example.appointmentsystem.entity.Appointment;
import com.example.appointmentsystem.service.AppointmentService;
import lombok.RequiredArgsConstructor;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patient/appointments")
@RequiredArgsConstructor
public class PatientAppointmentController {

	private final AppointmentService appointmentService;

	/**
	 * Book a new appointment for the logged-in patient.
	 *
	 * @param doctorId       ID of the doctor
	 * @param slotId         ID of the availability slot
	 * @param authentication Spring Security object (injected automatically)
	 * @return Appointment entity
	 */
	@PostMapping
	public ResponseEntity<?> bookAppointment(@RequestBody AppointmentRequest request, Principal principal) {
		return ResponseEntity.ok(
				appointmentService.bookAppointment(principal.getName(), request.getDoctorId(), request.getSlotId()));
	}

//	@GetMapping
//	public ResponseEntity<List<Appointment>> getMyAppointments(Principal principal) {
//		return ResponseEntity.ok(appointmentService.getPatientAppointments(principal.getName()));
//	}
	
	
	@GetMapping("/getAppointments")
	public ResponseEntity<List<AppointmentResponseDTO>> getAppointments(
	        Principal principal) {

	    return ResponseEntity.ok(
	            appointmentService.getPatientAppointmentsData(principal.getName())
	    );
	}

	

	@PutMapping("/{appointmentId}/cancel")
	public ResponseEntity<?> cancelAppointment(
	        @PathVariable("appointmentId") Long appointmentId,
	        Principal principal
	) {
	    return ResponseEntity.ok(
	            appointmentService.cancelAppointment(
	                    principal.getName(),
	                    appointmentId
	            )
	    );
	}
	
	@PutMapping("/{appointmentId}/reschedule")
	public ResponseEntity<?> rescheduleAppointment(
	        @PathVariable("appointmentId") Long appointmentId,
	        @RequestBody RescheduleAppointmentRequest request,
	        Principal principal
	) {
		System.out.println("RESCHEDULE HIT FOR APPOINTMENT " + appointmentId);
	    return ResponseEntity.ok(
	            appointmentService.rescheduleAppointment(
	                    principal.getName(),
	                    appointmentId,
	                    request.getNewSlotId()
	            )
	    );
	}



}
