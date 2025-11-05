package com.api.modules.challenge.dto;

import com.api.common.enums.Category;
import com.api.common.enums.Frequency;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChallengeCreateDTO {
    
    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "La descripción es obligatoria")
    private String description;

    @NotNull(message = "La frecuencia es obligatoria")
    private Frequency frequency;

    @NotNull(message = "La categoría es obligatoria")
    private Category category;
    
    // El campo image puede ser opcional
    private String image;

    @NotNull(message = "Los puntos son obligatorios")
    @Min(value = 1, message = "Los puntos deben ser al menos 1")
    private Integer points;
    
}