package com.example.appointmentsystem.controller;

import com.example.appointmentsystem.service.NotificationDispatcherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationDispatcherService dispatcherService;

    // ✅ THIS is the API we were talking about
    @PostMapping("/dispatch")
    public ResponseEntity<String> dispatchNotifications() {
    	
    	System.out.println("Dispatch APi Hit");

        dispatcherService.dispatchPendingNotifications();
        return ResponseEntity.ok("Notifications dispatched successfully");
    }
}
