package com.api.modules.achievement.dto;

import com.api.common.enums.AchievementType;
import com.api.common.enums.Status;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AchievementCreateDTO {
    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "La descripción es obligatoria")
    private String description;

    private String phrase;

    @NotNull(message = "El tipo de logro es obligatorio")
    private AchievementType achievementType;

    private Integer points; // Opcional (solo para logros de usuario)

    private Status status = Status.ACTIVO;
}
