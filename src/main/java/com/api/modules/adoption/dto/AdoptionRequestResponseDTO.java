package com.api.modules.adoption.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.api.common.enums.Status; // <--- USAMOS EL ENUM MODIFICADO

import lombok.Data;

/**
 * DTO para responder con la información de una solicitud.
 * Incluye información anidada del solicitante y de la publicación.
 */
@Data
public class AdoptionRequestResponseDTO {
    
    private UUID id;
    private Status status; // <--- USAMOS EL ENUM MODIFICADO
    private LocalDateTime createdAt;
    private String message;

    // Información del Solicitante (Quién la envió)
    private ApplicantInfoDTO applicant;
    
    // Información de la Publicación (Para qué mascota)
    private PublicationInfoDTO publication;

    // --- Sub-DTOs internos ---

    @Data
    public static class ApplicantInfoDTO {
        private UUID userId;
        private String name;
        private String district;
        private String province;
    }

    @Data
    public static class PublicationInfoDTO {
        private UUID publicationId;
        private String petName; // tempName de la publicación
        private String petPhoto;
        
        // Información del Dueño (A quién pertenece la publicación)
        private UUID ownerId;
        private String ownerName;
    }
}