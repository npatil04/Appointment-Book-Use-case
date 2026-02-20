package com.example.appointmentsystem.service;

import com.example.appointmentsystem.entity.Notification;
import com.example.appointmentsystem.enums.NotificationStatus;
import com.example.appointmentsystem.enums.NotificationType;
import com.example.appointmentsystem.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationDispatcherService {

    private final NotificationRepository notificationRepository;
    private final SmsNotificationSender smsSender;

    public void dispatchPendingNotifications() {
    	
    	System.out.println("Starting dispatch of pending notifications");

        // 🔴 You faced this earlier → now we fix it
        List<Notification> notifications =
                notificationRepository.findByStatus(NotificationStatus.PENDING);

        for (Notification notification : notifications) {
        	
        	System.out.println("Processing notifications");
        	System.out.println("Notification ID:"+notification.getId());
        	System.out.println("Notification Type:"+notification.getType());
        	System.out.println("Notification USer Id:"+notification.getUser().getId());

            if (notification.getType() == NotificationType.SMS) {

                smsSender.send(
                        notification.getUser().getMobilenumber(),
                        notification.getMessage()
                );

                notification.setStatus(NotificationStatus.SENT);
                notification.setSentAt(LocalDateTime.now());
            }
        }

        notificationRepository.saveAll(notifications);
    }
}
