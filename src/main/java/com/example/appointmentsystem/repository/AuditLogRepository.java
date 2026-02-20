package com.example.appointmentsystem.repository;

import com.example.appointmentsystem.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
