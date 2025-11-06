package com.api.modules.adoption.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO para crear una nueva solicitud de adopción.
 * El 'applicantId' (solicitante) se obtendrá del token de seguridad.
 */
@Data
public class AdoptionRequestCreateDTO {
    
    @NotNull(message = "El ID de la publicación es obligatorio")
    private UUID publicationId;

    private String message; // Mensaje opcional
}