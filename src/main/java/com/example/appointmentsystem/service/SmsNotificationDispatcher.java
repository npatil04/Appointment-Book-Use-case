//package com.example.appointmentsystem.service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//import com.example.appointmentsystem.entity.Notification;
//import com.example.appointmentsystem.enums.NotificationStatus;
//import com.example.appointmentsystem.enums.NotificationType;
//import com.example.appointmentsystem.repository.NotificationRepository;
//
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//
//@Service
//@RequiredArgsConstructor
//public class SmsNotificationDispatcher {
//
//    private final NotificationRepository notificationRepository;
//    private final SmsNotificationSender smsSender;
//
//    @Transactional
//    public void dispatch() {
//
//        List<Notification> notifications =
//                notificationRepository.findByStatus(NotificationStatus.PENDING);
//
//        for (Notification notification : notifications) {
//
//            if (notification.getType() != NotificationType.SMS) {
//                continue;
//            }
//
//            String mobile = notification.getUser().getMobilenumber();
//            smsSender.send(mobile, notification.getMessage());
//
//            notification.setStatus(NotificationStatus.SENT);
//            notification.setSentAt(LocalDateTime.now());
//        }
//    }
//    
////    @Scheduled(fixedDelay = 5000)
////    public void processSmsNotifications() {
////        dispatch();
////    }
//}
//
