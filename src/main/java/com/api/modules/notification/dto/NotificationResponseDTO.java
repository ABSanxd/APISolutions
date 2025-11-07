package com.api.modules.notification.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.api.common.enums.NotificationChannel;
import com.api.common.enums.NotificationType;
import com.api.common.enums.Status;

import lombok.Data;

@Data
public class NotificationResponseDTO {
    private UUID id;
    private String title;
    private String message;
    private NotificationType type;
    private NotificationChannel channel;
    private Status status;
    private LocalDateTime createdAt;
}
