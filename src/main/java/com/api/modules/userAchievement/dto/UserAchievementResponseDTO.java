package com.api.modules.userAchievement.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.api.common.enums.AchievementType;


public record UserAchievementResponseDTO(
    // Datos del registro user_achievement
    UUID id,
    LocalDateTime completedAt,
    Integer timesCompleted,

    // Datos del usuario (solo ID y nombre son suficientes)
    UUID userId,
    String userName,

    // Datos del logro
    UUID achievementId,
    String achievementName,
    String achievementDescription,
    String achievementPhrase,
    AchievementType achievementType,
    Integer achievementPoints,
    Boolean repeatable,
    Integer requiredCount // ✅ Incluimos el nuevo campo
) {}