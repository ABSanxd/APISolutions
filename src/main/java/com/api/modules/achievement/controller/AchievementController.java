package com.api.modules.achievement.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.common.enums.AchievementType;
import com.api.common.enums.Status;
import com.api.common.response.ApiResponse;
import com.api.modules.achievement.dto.AchievementCreateDTO;
import com.api.modules.achievement.dto.AchievementResponseDTO;
import com.api.modules.achievement.dto.AchievementUpdateDTO;
import com.api.modules.achievement.service.AchievementService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/achievements")
@RequiredArgsConstructor
public class AchievementController {

    private final AchievementService achievementService;

    // crear logro
    @PostMapping
    public ResponseEntity<ApiResponse<AchievementResponseDTO>> createAchievement(
            @Valid @RequestBody AchievementCreateDTO dto) {
        AchievementResponseDTO achievement = achievementService.createAchievement(dto);
        return ResponseEntity.ok(ApiResponse.success(achievement, "Logro creado con éxito"));
    }

    // Obtener todos los logros
   @GetMapping
    public ResponseEntity<ApiResponse<List<AchievementResponseDTO>>> getAllAchievements(
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) AchievementType type) {
        
        List<AchievementResponseDTO> achievements = achievementService.getAchievements(status, type);
        
        return ResponseEntity.ok(ApiResponse.success(achievements, "Logros obtenidos correctamente"));
    }

    // Obtener logro por ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AchievementResponseDTO>> getAchievementById(@PathVariable UUID id) {
        AchievementResponseDTO achievement = achievementService.getAchievementById(id);
        return ResponseEntity.ok(ApiResponse.success(achievement, "Logro obtenido correctamente"));
    }

    // Actualizar logro
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AchievementResponseDTO>> updateAchievement(
            @PathVariable UUID id,
            @RequestBody AchievementUpdateDTO dto) {
        AchievementResponseDTO achievement = achievementService.updateAchievement(id, dto);
        return ResponseEntity.ok(ApiResponse.success(achievement, "Logro actualizado correctamente"));
    }

    // inactivar logro
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deactivateAchievement(@PathVariable UUID id) {
        achievementService.deactivateAchievement(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Logro inactivado correctamente"));
    } 
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> activateAchievement(@PathVariable UUID id) {
        achievementService.activateAchievement(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Logro activado correctamente"));
    } 

}
