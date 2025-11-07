package com.api.modules.notification.dto;

import java.util.UUID;

import com.api.common.enums.NotificationChannel;
import com.api.common.enums.NotificationType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NotificationCreateDTO {

    private UUID userId; // opcional si se obtiene del token

    @NotBlank(message = "El título es obligatorio")
    private String title;

    @NotBlank(message = "El mensaje es obligatorio")
    private String message;

    @NotNull(message = "El tipo de notificación es obligatorio")
    private NotificationType type;

    @NotNull(message = "El canal es obligatorio")
    private NotificationChannel channel;

    private String actionUrl;
}
