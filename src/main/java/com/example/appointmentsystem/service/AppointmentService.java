package com.example.appointmentsystem.service;

import com.example.appointmentsystem.dto.AppointmentResponseDTO;
import com.example.appointmentsystem.entity.*;
import com.example.appointmentsystem.enums.*;
import com.example.appointmentsystem.exception.*;
import com.example.appointmentsystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AvailabilitySlotRepository slotRepository;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final NotificationService notificationService;
    private final AuditLogService auditLogService;

    @Transactional
    public Appointment bookAppointment(String username, Long doctorId, Long slotId) {

        // Fetch patient
        User patient = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        if (patient.getStatus() != UserStatus.ACTIVE) {
            throw new BusinessException("Patient account is inactive");
        }

        // Fetch doctor
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        // Fetch availability slot
        AvailabilitySlot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new ResourceNotFoundException("Slot not found"));

        // Validate slot belongs to doctor
        if (!slot.getDoctor().getId().equals(doctor.getId())) {
            throw new BusinessException("Slot does not belong to this doctor");
        }

        // Validate slot availability
        if (slot.getStatus() != AvailabilityStatus.AVAILABLE) {
            throw new BusinessException("Slot already booked");
        }

        // Validate slot is not in the past
        if (slot.getStartTime().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Cannot book past slot");
        }

        // Create appointment
//        Appointment appointment = Appointment.builder()
//                .patient(patient)
//                .doctor(doctor)
//                .slot(slot)
//                .appointmentStatus(AppointmentStatus.BOOKED)
//                .build();
//
//        appointmentRepository.save(appointment);
        
        
        Optional<Appointment> existingAppointment =
                appointmentRepository.findBySlotId(slotId);

        Appointment appointment;

        if (existingAppointment.isPresent()) {
            appointment = existingAppointment.get();

            if (appointment.getAppointmentStatus() != AppointmentStatus.CANCELLED) {
                throw new BusinessException("Slot already has an active appointment");
            }

            // Re-book cancelled appointment
            appointment.setPatient(patient);
            appointment.setAppointmentStatus(AppointmentStatus.BOOKED);

        } else {
            appointment = Appointment.builder()
                    .patient(patient)
                    .doctor(doctor)
                    .slot(slot)
                    .appointmentStatus(AppointmentStatus.BOOKED)
                    .build();
        }

        appointmentRepository.save(appointment);


        // Update slot status
        slot.setStatus(AvailabilityStatus.BOOKED);
        slotRepository.save(slot);

        // Send notifications (POC)
        notificationService.notifyBooking(patient, doctor, appointment);

        // Audit log
        auditLogService.log(patient, AuditAction.CREATE, "APPOINTMENT", appointment.getId());

        return appointment;
    }
    
//    @Transactional(readOnly = true)
//    public List<Appointment> getPatientAppointments(String username) {
//
//        User patient = userRepository.findByUsername(username)
//                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
//
//        if (patient.getRole() != Role.PATIENT) {
//            throw new BusinessException("Only patients can view their appointments");
//        }
//
//        return appointmentRepository.findByPatient(patient);
//    }
    
    
    @Transactional
    public Appointment cancelAppointment(String username, Long appointmentId) {

        User patient = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        // Ensure the appointment belongs to the logged-in patient
        if (!appointment.getPatient().getId().equals(patient.getId())) {
            throw new BusinessException("You are not allowed to cancel this appointment");
        }

        // Only BOOKED appointments can be cancelled
        if (appointment.getAppointmentStatus() != AppointmentStatus.BOOKED) {
            throw new BusinessException("Only booked appointments can be cancelled");
        }

        // Update appointment status
        appointment.setAppointmentStatus(AppointmentStatus.CANCELLED);

        // Free the slot
        AvailabilitySlot slot = appointment.getSlot();
        slot.setStatus(AvailabilityStatus.AVAILABLE);
        slotRepository.save(slot);

        appointmentRepository.save(appointment);

        // Optional (POC but recommended)
        notificationService.notifyCancellation(
                appointment.getPatient(),
                appointment.getDoctor(),
                appointment
        );

        auditLogService.log(
                patient,
                AuditAction.UPDATE,
                "APPOINTMENT",
                appointment.getId()
        );

        return appointment;
    }
    
    @Transactional
    public Appointment rescheduleAppointment(
            String username,
            Long appointmentId,
            Long newSlotId
    ) {
        // 1️⃣ Get patient
        User patient = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        // 2️⃣ Get appointment
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        // 3️⃣ Ownership check
        if (!appointment.getPatient().getId().equals(patient.getId())) {
            throw new BusinessException("You are not allowed to reschedule this appointment");
        }

        // 4️⃣ Status check
        if (appointment.getAppointmentStatus() != AppointmentStatus.BOOKED) {
            throw new BusinessException("Only BOOKED appointments can be rescheduled");
        }

        // 5️⃣ Get new slot
        AvailabilitySlot newSlot = slotRepository.findById(newSlotId)
                .orElseThrow(() -> new ResourceNotFoundException("New slot not found"));

        // 6️⃣ Same doctor validation
        if (!newSlot.getDoctor().getId().equals(appointment.getDoctor().getId())) {
            throw new BusinessException("New slot must belong to the same doctor");
        }

        // 7️⃣ Slot availability check
        if (newSlot.getStatus() != AvailabilityStatus.AVAILABLE) {
            throw new BusinessException("Selected slot is not available");
        }
        
//        if (oldSlot.getId().equals(newSlot.getId())) {
//            throw new BusinessException("Cannot reschedule to the same slot");
//        }
        
        if (appointment.getAppointmentStatus() == AppointmentStatus.RESCHEDULED) {
            throw new BusinessException("Appointment already rescheduled");
        }

        // 8️⃣ Free old slot
        AvailabilitySlot oldSlot = appointment.getSlot();
        oldSlot.setStatus(AvailabilityStatus.AVAILABLE);
        slotRepository.save(oldSlot);

        // 9️⃣ Assign new slot
        newSlot.setStatus(AvailabilityStatus.BOOKED);
        slotRepository.save(newSlot);

        // 🔟 Update appointment
        appointment.setSlot(newSlot);
        appointment.setAppointmentStatus(AppointmentStatus.RESCHEDULED);

        appointmentRepository.save(appointment);

        // 🔔 Notifications
        notificationService.notifyReschedule(
                patient,
                appointment.getDoctor(),
                appointment
        );

        // 📝 Audit log
        auditLogService.log(
                patient,
                AuditAction.UPDATE,
                "APPOINTMENT",
                appointment.getId()
        );

        return appointment;
    }
    
    
    @Transactional(readOnly = true)
    public List<Appointment> getDoctorAppointments(String doctorUsername) {

        return appointmentRepository
                .findByDoctorUserUsernameOrderBySlotStartTimeAsc(doctorUsername);
    }
    
    
    
    @Transactional
    public Appointment updateAppointmentStatus(
            String doctorUsername,
            Long appointmentId,
            AppointmentStatus newStatus
    ) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        // Verify doctor ownership
        if (!appointment.getDoctor().getUser().getUsername().equals(doctorUsername)) {
            throw new BusinessException("You are not allowed to update this appointment");
        }

        // Validate current status
        AppointmentStatus currentStatus = appointment.getAppointmentStatus();

        if (currentStatus == AppointmentStatus.CANCELLED ||
            currentStatus == AppointmentStatus.COMPLETED ||
            currentStatus == AppointmentStatus.NO_SHOW) {
            throw new BusinessException("Appointment cannot be updated from current status");
        }

        // Allow only COMPLETED or NO_SHOW
        if (newStatus != AppointmentStatus.COMPLETED &&
            newStatus != AppointmentStatus.NO_SHOW) {
            throw new BusinessException("Invalid status update");
        }

        appointment.setAppointmentStatus(newStatus);

        // Audit log
        auditLogService.log(
        	    appointment.getDoctor().getUser(),
        	    AuditAction.UPDATE,
        	    "APPOINTMENT",
        	    appointment.getId()
        	);


        return appointmentRepository.save(appointment);
    }
    
    
    /**
     * Converts an Appointment JPA entity into a safe, frontend-facing DTO.
     *
     * <p>
     * This method is intentionally kept private because:
     * <ul>
     *   <li>It encapsulates entity-to-DTO mapping logic</li>
     *   <li>Prevents leaking internal entity structure to controllers</li>
     *   <li>Makes response shape consistent across APIs</li>
     * </ul>
     *
     * <p>
     * NOTE:
     * We never return Appointment entities directly from controllers
     * to avoid exposing sensitive user information (passwords, roles, etc.)
     */
    private AppointmentResponseDTO mapToDTO(Appointment appointment) {

        return AppointmentResponseDTO.builder()
                // Unique identifier of the appointment
                .appointmentId(appointment.getId())

                // Doctor information shown to the patient
                .doctorName(appointment.getDoctor().getUser().getUsername())
                .specialization(appointment.getDoctor().getSpecialization())

                // Slot timing details
                .startTime(appointment.getSlot().getStartTime())
                .endTime(appointment.getSlot().getEndTime())

                // Current status of the appointment (BOOKED, CANCELLED, RESCHEDULED, etc.)
                .status(appointment.getAppointmentStatus())

                .build();
    }

    /**
     * Fetches all appointments for the currently logged-in patient
     * and returns them in a DTO-based response format.
     *
     * <p>
     * Flow:
     * <ol>
     *   <li>Find the patient by username from the security context</li>
     *   <li>Fetch all appointments belonging to that patient</li>
     *   <li>Transform each Appointment entity into AppointmentResponseDTO</li>
     * </ol>
     *
     * <p>
     * This method ensures:
     * <ul>
     *   <li>Data isolation (patients can only see their own appointments)</li>
     *   <li>Clean API response structure</li>
     *   <li>No exposure of internal database entities</li>
     * </ul>
     *
     * @param username logged-in patient's username (from JWT / Principal)
     * @return list of patient appointments as DTOs
     * @throws ResourceNotFoundException if patient does not exist
     */
    public List<AppointmentResponseDTO> getPatientAppointmentsData(String username) {

        // Validate and fetch patient user
        User patient = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        // Fetch appointments and convert entities to DTOs
        return appointmentRepository
                .findByPatient(patient)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }







}
