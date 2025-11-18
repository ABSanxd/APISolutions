package com.api.modules.notification.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.api.modules.notification.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    // Página de notificaciones del usuario ordenadas por fecha
    Page<Notification> findByUserIdOrderByCreatedAtDesc(UUID userId, Pageable pageable);
}
