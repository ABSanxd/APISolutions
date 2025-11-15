package com.api.modules.petAchievement.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

//progreso detallado
public record AchievementProgressDTO(
    UUID achievementId,
    String achievementName,
    String achievementDescription,
    String achievementPhrase,
    //Integer totalPoints, no puntos por ahora
    Boolean repeatable,
    Boolean completed,
    Boolean countFromCreation,
    LocalDate periodStart,
    LocalDate periodEnd,
    String validationPeriod, // "SEMANAL", "MENSUAL", "TOTAL"
    List<RequirementProgressDTO> requirements
) {}