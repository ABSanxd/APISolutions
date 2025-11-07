package com.api.modules.vaccine.dto;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VaccineUpdateDTO {
    @NotBlank(message = "El nombre de la vacuna es obligatorio")
    private String name;

    @Valid
    private List<VaccineDoseDTO> doses; // Enviará la lista completa de dosis (nuevas y existentes)
}