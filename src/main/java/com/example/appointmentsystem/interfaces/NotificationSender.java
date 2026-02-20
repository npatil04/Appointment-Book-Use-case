package com.example.appointmentsystem.interfaces;

import com.example.appointmentsystem.entity.User;

public interface NotificationSender {
    void send(String mobileNumber, String message);
}

