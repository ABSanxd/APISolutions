package com.api.modules.achievementRequirement.dto;

import java.util.UUID;

import com.api.common.enums.ValidationPeriod;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AchievementRequirementCreateDTO(
    
    @NotNull(message = "El ID del logro es obligatorio")
    UUID achievementId,
    
    @NotNull(message = "El ID del reto es obligatorio")
    UUID challengeId,
    
    @NotNull(message = "El número de repeticiones es obligatorio")
    @Min(value = 1, message = "El número de repeticiones debe ser al menos 1")
    Integer repetitions,
    
    @NotNull(message = "El periodo de validación es obligatorio")
    ValidationPeriod validationPeriod
) {}