package com.api.modules.pet.dto;

import java.math.BigDecimal;

import com.api.common.enums.Species;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PetCreateDTO {
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @NotNull(message = "La especie es obligatoria")
    private Species especie;

    @Size(max = 100, message = "La raza no puede exceder 100 caracteres")
    private String breed;

    @Min(value = 0, message = "La edad no puede ser negativa")
    @Max(value = 100, message = "La edad no puede exceder 100 años")
    private Integer petAge;

    @DecimalMin(value = "0.0", message = "El peso debe ser positivo")
    @DecimalMax(value = "999.99", message = "El peso es demasiado grande")
    private BigDecimal petWeight;

    private String photo;
}