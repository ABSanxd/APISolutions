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

    private Integer points; //(solo para logros de usuario)

    @NotNull(message = "Es necesario especificar si el logro es único o repetible")
    private Boolean repeatable;

    private Status status = Status.ACTIVO;

    private Boolean countFromCreation = true; // true = contar retos completados desde creación, false = histórico
}
