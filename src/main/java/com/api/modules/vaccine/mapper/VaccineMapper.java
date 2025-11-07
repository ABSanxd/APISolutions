package com.api.modules.vaccine.mapper;

import com.api.modules.pet.model.Pet;
import com.api.modules.vaccine.dto.VaccineCreateDTO;
import com.api.modules.vaccine.dto.VaccineResponseDTO;
import com.api.modules.vaccine.model.Vaccine;
import java.util.stream.Collectors;

public class VaccineMapper {

    // Convertir DTO a Entidad (sin dosis)
    public static Vaccine toEntity(VaccineCreateDTO dto, Pet pet) {
        Vaccine vaccine = new Vaccine();
        vaccine.setName(dto.getName());
        vaccine.setPet(pet); // Vincular a la mascota
        return vaccine;
    }

    // Convertir Entidad a DTO (con dosis)
    public static VaccineResponseDTO toResponseDTO(Vaccine vaccine) {
        VaccineResponseDTO dto = new VaccineResponseDTO();
        dto.setId(vaccine.getId());
        dto.setName(vaccine.getName());
        dto.setStatus(vaccine.getStatus());
        dto.setCreatedAt(vaccine.getCreatedAt());
        dto.setUpdatedAt(vaccine.getUpdatedAt());
        
        // Mapear la lista de dosis
        if (vaccine.getDoses() != null) {
            dto.setDoses(vaccine.getDoses().stream()
                .map(VaccineDoseMapper::toResponseDTO)
                .collect(Collectors.toList()));
        }
        return dto;
    }
}