package com.api.modules.notification.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.modules.notification.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findByUserIdOrderByCreatedAtDesc(UUID userId);
}
