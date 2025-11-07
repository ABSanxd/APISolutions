package com.api.modules.notification.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.api.common.enums.NotificationChannel;
import com.api.common.enums.NotificationType;
import com.api.common.enums.Status;
import com.api.modules.user.model.User;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationChannel channel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status; // Ej: PENDIENTE, ENVIADO, LEÍDA

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private String actionUrl;
}
