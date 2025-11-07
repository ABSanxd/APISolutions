package com.api.modules.petchallenge.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.common.response.ApiResponse;
import com.api.modules.petchallenge.dto.PetChallengeCreateDTO;
import com.api.modules.petchallenge.dto.PetChallengeResponseDTO;
import com.api.modules.petchallenge.service.PetChallengeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/pets/{petId}/challenges")
@RequiredArgsConstructor
public class petChallengeController {
    private final PetChallengeService petChallengeService;

    // Marcar reto como completado
    @PostMapping
    public ResponseEntity<ApiResponse<PetChallengeResponseDTO>> completeChallenge(
            @PathVariable UUID petId,
            @Valid @RequestBody PetChallengeCreateDTO dto) {
        PetChallengeResponseDTO response = petChallengeService.completeChallenge(petId, dto);
        return ResponseEntity.ok(ApiResponse.success(response, "Reto completado exitosamente"));
    }

    // historial de retos ordenados por fecha
    @GetMapping
    public ResponseEntity<ApiResponse<List<PetChallengeResponseDTO>>> getPetChallengeHistory(
            @PathVariable UUID petId) {

        List<PetChallengeResponseDTO> history = petChallengeService.getPetChallengeHistory(petId);
        return ResponseEntity.ok(ApiResponse.success(history, "Historial de retos obtenido"));
    }

    // retos completados HOYY
    @GetMapping("/today")
    public ResponseEntity<ApiResponse<List<PetChallengeResponseDTO>>> getPetChallengesToday(
            @PathVariable UUID petId) {

        List<PetChallengeResponseDTO> challenges = petChallengeService.getPetChallengesToday(petId);
        return ResponseEntity.ok(ApiResponse.success(challenges, "Retos de hoy obtenidos"));
    }

    // Retos completados esta semana
    @GetMapping("/week")
    public ResponseEntity<ApiResponse<List<PetChallengeResponseDTO>>> getPetChallengesThisWeek(
            @PathVariable UUID petId) {

        List<PetChallengeResponseDTO> challenges = petChallengeService.getPetChallengesThisWeek(petId);
        return ResponseEntity.ok(ApiResponse.success(challenges, "Retos de la semana obtenidos"));
    }

}
