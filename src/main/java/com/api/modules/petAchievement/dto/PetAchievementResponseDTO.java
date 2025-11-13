package com.api.modules.petAchievement.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.api.common.enums.AchievementType;
import com.api.common.enums.Status;

public record PetAchievementResponseDTO(
    UUID id,
    UUID petId,
    String petName,
    UUID achievementId,
    String achievementName,
    String achievementDescription,
    String achievementPhrase,
    AchievementType achievementType,
    //Integer achievementPoints, no puntos por ahora
    Boolean repeatable,
    LocalDate periodStart,
    LocalDate periodEnd,
    Status status,
    LocalDateTime completedAt,
    LocalDateTime createdAt
) {}