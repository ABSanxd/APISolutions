package com.api.modules.adoption.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.common.enums.Status;
import com.api.common.response.ApiResponse;
import com.api.modules.adoption.dto.AdoptionRequestCreateDTO;
import com.api.modules.adoption.dto.AdoptionRequestResponseDTO;
import com.api.modules.adoption.service.AdoptionRequestService;
import com.api.modules.user.model.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/adoption-requests")
@RequiredArgsConstructor
public class AdoptionRequestController {
    
    private final AdoptionRequestService adoptionRequestService;

    /**
     * Endpoint para crear una nueva solicitud de adopción (POST /)
     * (Lógica del Paso 4 - ya implementada)
     */
    @PostMapping
    public ResponseEntity<ApiResponse<AdoptionRequestResponseDTO>> createAdoptionRequest(
            @AuthenticationPrincipal User applicantUser,
            @Valid @RequestBody AdoptionRequestCreateDTO dto
    ) {
        if (applicantUser == null) {
            return ResponseEntity.status(401)
                .body(ApiResponse.fail("Usuario no autenticado", 401));
        }
        ApiResponse<AdoptionRequestResponseDTO> response = 
            adoptionRequestService.createRequest(applicantUser, dto);

        if ("fail".equals(response.getStatus())) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    // --- INICIO DE NUEVOS ENDPOINTS (PASO 6) ---

    /**
     * Obtiene las solicitudes que el usuario autenticado ha RECIBIDO.
     * (Para "Mis Solicitudes" -> "Recibidas")
     */
    @GetMapping("/received")
    public ResponseEntity<ApiResponse<List<AdoptionRequestResponseDTO>>> getReceivedRequests(
            @AuthenticationPrincipal User ownerUser
    ) {
        if (ownerUser == null) {
            return ResponseEntity.status(401).body(ApiResponse.fail("Usuario no autenticado", 401));
        }
        return ResponseEntity.ok(adoptionRequestService.getReceivedRequests(ownerUser));
    }

    /**
     * Obtiene las solicitudes que el usuario autenticado ha ENVIADO.
     * (Para "Mis Solicitudes" -> "Enviadas")
     */
    @GetMapping("/sent")
    public ResponseEntity<ApiResponse<List<AdoptionRequestResponseDTO>>> getSentRequests(
            @AuthenticationPrincipal User applicantUser
    ) {
        if (applicantUser == null) {
            return ResponseEntity.status(401).body(ApiResponse.fail("Usuario no autenticado", 401));
        }
        return ResponseEntity.ok(adoptionRequestService.getSentRequests(applicantUser));
    }

    /**
     * ACEPTA una solicitud. (Botón Aceptar)
     * Solo el dueño de la publicación puede hacer esto.
     */
    @PatchMapping("/{id}/accept")
    public ResponseEntity<ApiResponse<AdoptionRequestResponseDTO>> acceptRequest(
            @AuthenticationPrincipal User ownerUser,
            @PathVariable UUID id
    ) {
        if (ownerUser == null) {
            return ResponseEntity.status(401).body(ApiResponse.fail("Usuario no autenticado", 401));
        }
        ApiResponse<AdoptionRequestResponseDTO> response = 
            adoptionRequestService.updateRequestStatus(id, Status.ACEPTADO, ownerUser);
        
        if ("fail".equals(response.getStatus())) {
            return ResponseEntity.status(response.getCode()).body(response);
        }
        return ResponseEntity.ok(response);
    }

    /**
     * RECHAZA una solicitud. (Botón Rechazar)
     * Solo el dueño de la publicación puede hacer esto.
     */
    @PatchMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<AdoptionRequestResponseDTO>> rejectRequest(
            @AuthenticationPrincipal User ownerUser,
            @PathVariable UUID id
    ) {
        if (ownerUser == null) {
            return ResponseEntity.status(401).body(ApiResponse.fail("Usuario no autenticado", 401));
        }
        ApiResponse<AdoptionRequestResponseDTO> response = 
            adoptionRequestService.updateRequestStatus(id, Status.RECHAZADO, ownerUser);
        
        if ("fail".equals(response.getStatus())) {
            return ResponseEntity.status(response.getCode()).body(response);
        }
        return ResponseEntity.ok(response);
    }

    /**
     * CANCELA una solicitud. (Botón Cancelar)
     * Solo el solicitante que la envió puede hacer esto.
     */
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<AdoptionRequestResponseDTO>> cancelRequest(
            @AuthenticationPrincipal User applicantUser,
            @PathVariable UUID id
    ) {
        if (applicantUser == null) {
            return ResponseEntity.status(401).body(ApiResponse.fail("Usuario no autenticado", 401));
        }
        ApiResponse<AdoptionRequestResponseDTO> response = 
            adoptionRequestService.updateRequestStatus(id, Status.CANCELADO, applicantUser);
        
        if ("fail".equals(response.getStatus())) {
            return ResponseEntity.status(response.getCode()).body(response);
        }
        return ResponseEntity.ok(response);
    }
}