package com.example.appointmentsystem.service;

import com.example.appointmentsystem.entity.*;
import com.example.appointmentsystem.enums.*;
import com.example.appointmentsystem.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    // ✅ BOOKING
    public void notifyBooking(User patient, Doctor doctor, Appointment appointment) {

        Notification patientNotification = Notification.builder()
                .user(patient)
                .type(NotificationType.SMS)
                .status(NotificationStatus.PENDING)
                .triggeredBy(NotificationTrigger.BOOKING)
                .message("Your appointment is booked successfully")
                .build();

        
        Notification doctorNotification = Notification.builder()
                .user(doctor.getUser())
                .type(NotificationType.EMAIL)
                .status(NotificationStatus.PENDING)
                .triggeredBy(NotificationTrigger.BOOKING)
                .message("A new appointment has been booked")
                .build();

        notificationRepository.save(patientNotification);
        notificationRepository.save(doctorNotification);
    }

    // ✅ CANCELLATION
    public void notifyCancellation(User patient, Doctor doctor, Appointment appointment) {

        Notification patientNotification = Notification.builder()
                .user(patient)
                .type(NotificationType.EMAIL)
                .status(NotificationStatus.PENDING)
                .triggeredBy(NotificationTrigger.CANCELLATION)
                .message("Your appointment has been cancelled")
                .build();

        Notification doctorNotification = Notification.builder()
                .user(doctor.getUser())
                .type(NotificationType.EMAIL)
                .status(NotificationStatus.PENDING)
                .triggeredBy(NotificationTrigger.CANCELLATION)
                .message("An appointment has been cancelled")
                .build();

        notificationRepository.save(patientNotification);
        notificationRepository.save(doctorNotification);
    }

    // ✅ (Optional but recommended) RESCHEDULE
    public void notifyReschedule(User patient, Doctor doctor, Appointment appointment) {

        Notification patientNotification = Notification.builder()
                .user(patient)
                .type(NotificationType.EMAIL)
                .status(NotificationStatus.PENDING)
                .triggeredBy(NotificationTrigger.RESCHEDULE)
                .message("Your appointment has been rescheduled")
                .build();

        Notification doctorNotification = Notification.builder()
                .user(doctor.getUser())
                .type(NotificationType.EMAIL)
                .status(NotificationStatus.PENDING)
                .triggeredBy(NotificationTrigger.RESCHEDULE)
                .message("An appointment has been rescheduled")
                .build();

        notificationRepository.save(patientNotification);
        notificationRepository.save(doctorNotification);
    }
}
