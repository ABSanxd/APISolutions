package com.api.modules.pet.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import com.api.common.enums.PetLevel;
import com.api.common.enums.Especies;
import com.api.common.enums.Status;
import lombok.Data;

@Data
public class PetResponseDTO {
    
    private UUID id;
    private UUID userId;
    private String nombre;
    private Especies especie;
    private PetLevel nivel;
    private Integer petXp;
    private String breed;
    
    private Integer ageYears;
    private Integer ageMonths;
    private LocalDate birthDate;
    
    private BigDecimal petWeight;
    private String photo;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedIn;
}