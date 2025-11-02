package com.api.modules.challenge.controller;

import com.api.common.response.ApiResponse;
import com.api.modules.challenge.dto.ChallengeCreateDTO;
import com.api.modules.challenge.dto.ChallengeDTO;
import com.api.modules.challenge.dto.ChallengeUpdateDTO;
import com.api.modules.challenge.service.ChallengeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import com.api.common.enums.Category;
import com.api.common.enums.Frequency;

import org.springframework.http.HttpStatus;
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

    // Metodo Agregado: POST /api/v1/challenges
    @PostMapping
    public ResponseEntity<ApiResponse<ChallengeDTO>> createChallenge(
            @Valid @RequestBody ChallengeCreateDTO challengeDto) {

        ChallengeDTO createdChallenge = challengeService.createChallenge(challengeDto);

        return ResponseEntity
                .status(HttpStatus.CREATED) // Código HTTP 201
                .body(ApiResponse.success(createdChallenge, "Reto creado exitosamente."));
    }

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
    // Marca un reto como completado para una mascota, actualiza XP y verifica
    // logros.
    @PostMapping("/pets/{petId}/complete/{challengeId}")
    public ResponseEntity<ApiResponse<Void>> completeChallenge(
            @PathVariable UUID petId,
            @PathVariable UUID challengeId) {

        challengeService.completeChallenge(petId, challengeId);

        return ResponseEntity
                .ok(ApiResponse.success(null, "Reto completado, XP y progreso de logros actualizado."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ChallengeDTO>> updateChallenge(
            @PathVariable UUID id,
            @Valid @RequestBody ChallengeUpdateDTO challengeDto) {

        ChallengeDTO updatedChallenge = challengeService.updateChallenge(id, challengeDto);
        return ResponseEntity.ok(ApiResponse.success(updatedChallenge, "Reto actualizado correctamente."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteChallenge(@PathVariable UUID id) {
        challengeService.deleteChallenge(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT) // Código HTTP 204
                .body(ApiResponse.success(null, "Reto eliminado correctamente."));
    }

}