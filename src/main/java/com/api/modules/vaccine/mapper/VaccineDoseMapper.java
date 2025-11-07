package com.api.modules.vaccine.mapper;

import com.api.modules.vaccine.dto.VaccineDoseDTO;
import com.api.modules.vaccine.model.Vaccine;
import com.api.modules.vaccine.model.VaccineDose;

public class VaccineDoseMapper {

    // Convertir DTO a Entidad
    public static VaccineDose toEntity(VaccineDoseDTO dto, Vaccine vaccine) {
        VaccineDose dose = new VaccineDose();
        dose.setApplicationDate(dto.getApplicationDate());
        dose.setApplied(dto.isApplied());
        dose.setVaccine(vaccine); // Vincular a la vacuna padre
        return dose;
    }

    // Convertir Entidad a DTO
    public static VaccineDoseDTO toResponseDTO(VaccineDose dose) {
        VaccineDoseDTO dto = new VaccineDoseDTO();
        dto.setId(dose.getId());
        dto.setApplicationDate(dose.getApplicationDate());
        dto.setApplied(dose.isApplied());
        return dto;
    }
}