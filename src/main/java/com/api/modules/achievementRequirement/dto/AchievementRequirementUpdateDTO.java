package com.api.modules.achievementRequirement.dto;

import java.util.UUID;

import com.api.common.enums.ValidationPeriod;

import jakarta.validation.constraints.Min;

public record AchievementRequirementUpdateDTO(
    
    UUID challengeId,
    
    @Min(value = 1, message = "El número de repeticiones debe ser al menos 1")
    Integer repetitions,
    
    ValidationPeriod validationPeriod
) {}