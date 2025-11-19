package com.api.modules.notification.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.api.common.enums.NotificationChannel;
import com.api.common.enums.NotificationType;
import com.api.common.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Schema(description = "Respuesta con los datos de una notificación")
@Data
public class NotificationResponseDTO {
  @Schema(description = "ID único de la notificación", example = "123e4567-e89b-12d3-a456-426614174000")
  private UUID id;

  @Schema(description = "Título de la notificación", example = "¡Nueva mascota disponible para adopción!")
  private String title;

  @Schema(description = "Mensaje descriptivo", example = "Tenemos una nueva mascota llamada 'Max' esperando por un hogar.")
  private String message;

  @Schema(description = "Tipo de notificación", example = "INFO")

  private NotificationType type;

  @Schema(description = "Canal de envío", example = "PUSH")
  private NotificationChannel channel;

  @Schema(description = "Estado de la notificación", example = "ENVIADO", allowableValues = { "PENDIENTE", "ENVIADO",
      "LEIDO" })
  private Status status;

  @Schema(description = "Fecha de creación", example = "2024-11-18T10:30:00")
  private LocalDateTime createdAt;

  @Schema(description = "URL de acción opcional", example = "/mascotas/123e4567-e89b-12d3-a456-426614174000")
  private String actionUrl; // JIJIJI FALTA ESTO QUE JUERRRTE
}
