package com.example.appointmentsystem.repository;

import com.example.appointmentsystem.entity.Notification;
import com.example.appointmentsystem.entity.User;
import com.example.appointmentsystem.enums.NotificationStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUser(User user);
    List<Notification> findByStatus(NotificationStatus status);
}
