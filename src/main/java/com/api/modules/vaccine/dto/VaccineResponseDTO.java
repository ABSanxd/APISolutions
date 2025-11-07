package com.api.modules.vaccine.dto;

import java.time.LocalDate; // <-- CAMBIO 1: Importar LocalDate
import java.util.List;
import java.util.UUID;
import com.api.common.enums.Status;
import lombok.Data;

@Data
public class VaccineResponseDTO {
    private UUID id;
    private String name;
    private Status status;
    private LocalDate createdAt; 
    private LocalDate updatedAt; 
    private List<VaccineDoseDTO> doses; 
}