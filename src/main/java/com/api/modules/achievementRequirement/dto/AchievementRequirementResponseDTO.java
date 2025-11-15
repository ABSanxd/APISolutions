package com.api.modules.achievementRequirement.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.api.common.enums.Category;
import com.api.common.enums.ValidationPeriod;

public record AchievementRequirementResponseDTO(
    UUID id,
    UUID achievementId,
    String achievementName, // Útil para el front
    UUID challengeId,
    String challengeName,   // Útil para el front
    Category challengeCategory, // Útil para el front
    Integer repetitions,
    ValidationPeriod validationPeriod,
    LocalDateTime createdAt
) {}
