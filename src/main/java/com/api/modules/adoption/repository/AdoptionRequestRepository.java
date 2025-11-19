package com.api.modules.adoption.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.api.common.enums.Status; // <--- USAMOS EL ENUM MODIFICADO
import com.api.modules.adoption.model.AdoptionRequest;

public interface AdoptionRequestRepository extends JpaRepository<AdoptionRequest, UUID> {

    // --- Búsquedas para el Solicitante (El que quiere adoptar) ---

    /**
     * Busca las solicitudes que un usuario ha ENVIADO.
     * (Para la pestaña "Mis Solicitudes" -> "Enviadas")
     */
    List<AdoptionRequest> findByApplicantId(UUID applicantId);

    /**
     * Verifica si un usuario (applicantId) ya tiene una solicitud PENDIENTE
     * para una publicación específica (publicationId).
     * Evita que envíe 10 solicitudes por la misma mascota.
     */
    boolean existsByApplicantIdAndPublicationIdAndStatus(UUID applicantId, UUID publicationId, Status status); // <---
                                                                                                               // USAMOS
                                                                                                               // EL
                                                                                                               // ENUM
                                                                                                               // MODIFICADO

    // --- Búsquedas para el Dueño de la Publicación ---

    /**
     * Busca las solicitudes que un usuario ha RECIBIDO en sus publicaciones.
     * (Para la pestaña "Mis Solicitudes" -> "Recibidas")
     * 
     * @param ownerId El ID del Dueño de las publicaciones.
     */
    @Query("SELECT ar FROM AdoptionRequest ar WHERE ar.publication.user.id = :ownerId")
    List<AdoptionRequest> findRequestsReceivedByOwnerId(@Param("ownerId") UUID ownerId);

    // --- AÑADIR ESTE MÉTODO ---
    /**
     * Busca todas las solicitudes PENDIENTES para una publicación,
     * excluyendo la solicitud que estamos a punto de aceptar.
     * Esto es para rechazarlas automáticamente.
     */
    List<AdoptionRequest> findByPublicationIdAndStatusAndIdNot(UUID publicationId, Status status,
            UUID acceptedRequestId);

    // cuenta numero de adopciones completadas por un usuario
    long countByApplicantIdAndStatus(UUID applicantId, Status status);
}