package com.api.modules.vaccine.dto;

import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.Valid;
import lombok.Data;

@Data
public class VaccineCreateDTO {
    @NotBlank(message = "El nombre de la vacuna es obligatorio")
    private String name;

    // Permite crear la vacuna CON sus dosis al mismo tiempo
    @Valid 
    private List<VaccineDoseDTO> doses;
}