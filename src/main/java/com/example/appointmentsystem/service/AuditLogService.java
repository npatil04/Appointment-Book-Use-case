package com.example.appointmentsystem.service;

import com.example.appointmentsystem.entity.AuditLog;
import com.example.appointmentsystem.entity.User;
import com.example.appointmentsystem.enums.AuditAction;
import com.example.appointmentsystem.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public void log(User user,
                    AuditAction action,
                    String entityName,
                    Long entityId) {

        AuditLog log = AuditLog.builder()
                .user(user)
                .action(action)
                .entityName(entityName)
                .entityId(entityId)
                .build();

        auditLogRepository.save(log);
    }
}
