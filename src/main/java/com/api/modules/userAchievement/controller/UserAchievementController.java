package com.api.modules.userAchievement.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.common.response.ApiResponse;
import com.api.modules.userAchievement.dto.UserAchievementResponseDTO;
import com.api.modules.userAchievement.service.UserAchievementService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users/{userId}/achievements")
@RequiredArgsConstructor
public class UserAchievementController {

    private final UserAchievementService userAchievementService;

    // Obtener todos los logros COMPLETADOS de un usuario
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserAchievementResponseDTO>>> getUserAchievements(
            @PathVariable UUID userId) {
        List<UserAchievementResponseDTO> achievements = userAchievementService.getUserAchievementsDTO(userId);
        return ResponseEntity.ok(ApiResponse.success(
                achievements,
                achievements.size() + " logros de usuario obtenidos correctamente"));
    }

    // Contar logros completados

    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> countUserAchievements(@PathVariable UUID userId) {
        long count = userAchievementService.countUserAchievements(userId);
        return ResponseEntity.ok(ApiResponse.success(
                count,
                "Cantidad de logros de usuario obtenida correctamente"));
    }
}