package com.api.modules.challenge.controller;

import com.api.common.response.ApiResponse;
import com.api.modules.challenge.dto.ChallengeDTO;
import com.api.modules.challenge.service.ChallengeService;

import lombok.RequiredArgsConstructor;

import com.api.common.enums.Category;
import com.api.common.enums.Frequency;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;


//punto de entrada para consultar retos y registrar la accion de completarlos

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/challenges")
public class ChallengeController {
private final ChallengeService challengeService;
    
    // gET /api/v1/challenges
    // Obtiene una lista de retos, con filtrado opcional por categoría y frecuencia.
    @GetMapping
    public ResponseEntity<ApiResponse<List<ChallengeDTO>>> getAllChallenges(
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) Frequency frequency) {

        List<ChallengeDTO> challenges = challengeService.findAllChallenges(category, frequency);
        return ResponseEntity.ok(ApiResponse.success(challenges, "Catálogo de retos obtenido correctamente."));
    }

    // GET /api/v1/challenges/{id}
    // Obtiene los detalles de un reto específico.
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ChallengeDTO>> getChallengeById(@PathVariable UUID id) {
        ChallengeDTO challenge = challengeService.getChallengeById(id);
        return ResponseEntity.ok(ApiResponse.success(challenge, "Reto encontrado exitosamente."));
    }

    // POST /api/v1/challenges/pets/{petId}/complete/{challengeId}
    // Marca un reto como completado para una mascota, actualiza XP y verifica logros.
    @PostMapping("/pets/{petId}/complete/{challengeId}")
    public ResponseEntity<ApiResponse<Void>> completeChallenge(
            @PathVariable UUID petId,
            @PathVariable UUID challengeId) {
        
        challengeService.completeChallenge(petId, challengeId);
        
        return ResponseEntity
                .ok(ApiResponse.success(null, "Reto completado, XP y progreso de logros actualizado."));
    }
}