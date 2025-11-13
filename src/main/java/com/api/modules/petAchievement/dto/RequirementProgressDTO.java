package com.api.modules.petAchievement.dto;

import java.util.UUID;

//PROGRESO DE CADA REQUISITO EN CADA LOGRO
public record RequirementProgressDTO(
    UUID requirementId,
    UUID challengeId,
    String challengeName,
    String challengeCategory,
    Integer currentProgress,  // 5 de 7
    Integer totalRequired,    // 7
    Double progressPercentage, // 71.4%
    Boolean completed
) {}