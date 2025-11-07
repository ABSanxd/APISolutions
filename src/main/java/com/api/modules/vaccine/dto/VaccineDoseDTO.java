package com.api.modules.vaccine.dto;

import java.time.LocalDate; 
import java.util.UUID;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VaccineDoseDTO {
    private UUID id; 

    @NotNull(message = "La fecha de aplicación es obligatoria")
    private LocalDate applicationDate; 

    private boolean applied = false;
}