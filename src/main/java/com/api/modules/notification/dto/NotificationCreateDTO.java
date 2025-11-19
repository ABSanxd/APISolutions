package com.api.modules.notification.dto;

import java.util.UUID;

import com.api.common.enums.NotificationChannel;
import com.api.common.enums.NotificationType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NotificationCreateDTO {

    @Schema(
        description = "ID del usuario destinatario (opcional, se usa el usuario autenticado si no se proporciona)",
        example = "123e4567-e89b-12d3-a456-426614174000"
    )
    private UUID userId; // opcional si se obtiene del token

    @NotBlank(message = "El título es obligatorio")
    @Schema(
        description = "Título de la notificación",
        example = "¡Nueva mascota disponible para adopción!",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String title;

    @NotBlank(message = "El mensaje es obligatorio")
    @Schema(
        description = "Mensaje descriptivo de la notificación",
        example = "Tenemos una nueva mascota llamada 'Max' esperando por un hogar. ¡Ven a conocerlo!",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String message;

    @NotNull(message = "El tipo de notificación es obligatorio")
    @Schema(
        description = "Tipo de notificación",
        example = "INFO",
        allowableValues = {"INFO", "ALERTA", "RECORDATORIO", "LOGRO"},
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private NotificationType type;

    @NotNull(message = "El canal es obligatorio")
    @Schema(
        description = "Canal por el cual se enviará la notificación",
        example = "PUSH",
        allowableValues = {"INTERNO", "EMAIL", "PUSH", "BOTH"},
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private NotificationChannel channel;

    @Schema(
        description = "URL de acción opcional (ej: link a la mascota, vacuna, etc.)",
        example = "/mascotas/123e4567-e89b-12d3-a456-426614174000"
    )
    private String actionUrl;
}
