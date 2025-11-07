package com.api.modules.vaccine.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import com.api.modules.pet.model.Pet;
import com.api.modules.pet.repository.PetRepository;
import com.api.modules.vaccine.dto.VaccineCreateDTO;
import com.api.modules.vaccine.dto.VaccineDoseDTO;
import com.api.modules.vaccine.dto.VaccineResponseDTO;
import com.api.modules.vaccine.dto.VaccineUpdateDTO; 
import com.api.modules.vaccine.mapper.VaccineDoseMapper;
import com.api.modules.vaccine.mapper.VaccineMapper;
import com.api.modules.vaccine.model.Vaccine;
import com.api.modules.vaccine.model.VaccineDose;
import com.api.modules.vaccine.repository.VaccineDoseRepository;
import com.api.modules.vaccine.repository.VaccineRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VaccineService {

    private final VaccineRepository vaccineRepository;
    private final VaccineDoseRepository vaccineDoseRepository;
    private final PetRepository petRepository;


    public List<VaccineResponseDTO> getVaccinesByPet(UUID petId) {
        return vaccineRepository.findByPetId(petId).stream()
                .map(VaccineMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public VaccineResponseDTO createVaccine(VaccineCreateDTO dto, UUID petId, UUID userId) {
        //Verificar que la mascota existe y pertenece al usuario
        Pet pet = petRepository.findByIdAndUserId(petId, userId)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada o no pertenece al usuario"));

        Vaccine vaccine = VaccineMapper.toEntity(dto, pet);
        Vaccine savedVaccine = vaccineRepository.save(vaccine);

        // Crear y guardar las dosis asociadas (si existen)
        if (dto.getDoses() != null && !dto.getDoses().isEmpty()) {
            List<VaccineDose> doses = dto.getDoses().stream()
                    .map(doseDto -> VaccineDoseMapper.toEntity(doseDto, savedVaccine))
                    .collect(Collectors.toList());
            vaccineDoseRepository.saveAll(doses);
            savedVaccine.setDoses(doses);
        }

        return VaccineMapper.toResponseDTO(savedVaccine);
    }

    public VaccineResponseDTO updateVaccine(UUID vaccineId, VaccineUpdateDTO dto, UUID petId, UUID userId) {
        Vaccine vaccine = vaccineRepository.findByIdAndPetId(vaccineId, petId)
                .orElseThrow(() -> new RuntimeException("Vacuna no encontrada o no pertenece a esta mascota"));

        vaccine.setName(dto.getName());

        vaccine.getDoses().clear();

        if (dto.getDoses() != null && !dto.getDoses().isEmpty()) {
            List<VaccineDose> newDoses = dto.getDoses().stream()
                .map(doseDto -> {
                    VaccineDose dose = VaccineDoseMapper.toEntity(doseDto, vaccine);
                    dose.setId(doseDto.getId()); 
                    return dose;
                })
                .collect(Collectors.toList());
            
           
            vaccine.getDoses().addAll(newDoses);
        }

        Vaccine updatedVaccine = vaccineRepository.save(vaccine);

        return VaccineMapper.toResponseDTO(updatedVaccine);
    }
    
    public void deleteVaccine(UUID vaccineId, UUID petId, UUID userId) {
        // Verificar que la mascota existe y pertenece al usuario
        if (!petRepository.existsById(petId)) {
             throw new RuntimeException("Mascota no encontrada");
        }
        
        Vaccine vaccine = vaccineRepository.findByIdAndPetId(vaccineId, petId)
                 .orElseThrow(() -> new RuntimeException("Vacuna no encontrada o no pertenece a esta mascota"));
        
       
        vaccineRepository.delete(vaccine);
    }


    public VaccineDoseDTO addDoseToVaccine(VaccineDoseDTO dto, UUID vaccineId) {
        Vaccine vaccine = vaccineRepository.findById(vaccineId)
                .orElseThrow(() -> new RuntimeException("Vacuna no encontrada"));

        VaccineDose dose = VaccineDoseMapper.toEntity(dto, vaccine);
        VaccineDose savedDose = vaccineDoseRepository.save(dose);
        return VaccineDoseMapper.toResponseDTO(savedDose);
    }

    public void deleteDose(UUID doseId) {
        if (!vaccineDoseRepository.existsById(doseId)) {
            throw new RuntimeException("Dosis no encontrada");
        }
        vaccineDoseRepository.deleteById(doseId);
    }
    
    public VaccineDoseDTO updateDoseStatus(UUID doseId, boolean applied) {
        VaccineDose dose = vaccineDoseRepository.findById(doseId)
                .orElseThrow(() -> new RuntimeException("Dosis no encontrada"));
        
        dose.setApplied(applied);
        VaccineDose updatedDose = vaccineDoseRepository.save(dose);
        return VaccineDoseMapper.toResponseDTO(updatedDose);
    }
}