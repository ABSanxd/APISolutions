package com.api.modules.petAchievement.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.common.response.ApiResponse;
import com.api.modules.petAchievement.dto.AchievementProgressDTO;
import com.api.modules.petAchievement.dto.PetAchievementResponseDTO;
import com.api.modules.petAchievement.service.PetAchievementProgressService;
import com.api.modules.petAchievement.service.PetAchievementService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/pets/{petId}/achievements")
@RequiredArgsConstructor
public class PetAchievementController {

    private final PetAchievementService petAchievementService;
    private final PetAchievementProgressService progressService;

    //Obtener todos los logros COMPLETADOS de una mascota
     
    @GetMapping("/completed")
    public ResponseEntity<ApiResponse<List<PetAchievementResponseDTO>>> getCompletedAchievements(
            @PathVariable UUID petId) {
        List<PetAchievementResponseDTO> achievements = petAchievementService.getCompletedAchievements(petId);
        return ResponseEntity.ok(ApiResponse.success(
            achievements,
            achievements.size() + " logros completados obtenidos correctamente"
        ));
    }

    /**
     * Obtener logros EN PROGRESO de una mascota
     * /api/v1/pets/{petId}/achievements/in-progress
     */
    @GetMapping("/in-progress")
    public ResponseEntity<ApiResponse<List<PetAchievementResponseDTO>>> getInProgressAchievements(
            @PathVariable UUID petId) {
        List<PetAchievementResponseDTO> achievements = petAchievementService.getInProgressAchievements(petId);
        return ResponseEntity.ok(ApiResponse.success(
            achievements,
            achievements.size() + " logros en progreso obtenidos correctamente"
        ));
    }

    /**
     * Obtener progreso detallado de un logro específico
     * /api/v1/pets/{petId}/achievements/{achievementId}/progress
     */
    @GetMapping("/{achievementId}/progress")
    public ResponseEntity<ApiResponse<AchievementProgressDTO>> getAchievementProgress(
            @PathVariable UUID petId,
            @PathVariable UUID achievementId) {
        AchievementProgressDTO progress = progressService.getAchievementProgress(petId, achievementId);
        return ResponseEntity.ok(ApiResponse.success(
            progress,
            "Progreso del logro obtenido correctamente"
        ));
    }

    /**
     * Obtener progreso de TODOS los logros disponibles
     * /api/v1/pets/{petId}/achievements/progress
     */
    @GetMapping("/progress")
    public ResponseEntity<ApiResponse<List<AchievementProgressDTO>>> getAllAchievementsProgress(
            @PathVariable UUID petId) {
        List<AchievementProgressDTO> progress = progressService.getAllAchievementsProgress(petId);
        return ResponseEntity.ok(ApiResponse.success(
            progress,
            "Progreso de todos los logros obtenido correctamente"
        ));
    }

    /**
     * Contar logros completados
     * /api/v1/pets/{petId}/achievements/count
     */
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> countCompletedAchievements(@PathVariable UUID petId) {
        long count = petAchievementService.countCompletedAchievements(petId);
        return ResponseEntity.ok(ApiResponse.success(
            count,
            "Cantidad de logros completados obtenida correctamente"
        ));
    }
}
