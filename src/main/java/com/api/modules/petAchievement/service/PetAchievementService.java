package com.api.modules.petAchievement.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.common.enums.Status;
import com.api.common.exception.ResourceNotFoundException;
import com.api.modules.petAchievement.dto.PetAchievementResponseDTO;
import com.api.modules.petAchievement.mapper.PetAchievementMapper;
import com.api.modules.petAchievement.model.PetAchievement;
import com.api.modules.petAchievement.repository.PetAchievementRepository;
import com.api.modules.pet.service.PetService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PetAchievementService {

    private final PetAchievementRepository petAchievementRepository;
    private final PetService petService;

    // Obtener todos los logros COMPLETADOS de una mascota
    @Transactional(readOnly = true)
    public List<PetAchievementResponseDTO> getCompletedAchievements(UUID petId) {
        
        // Validar que la mascota exista
        petService.findById(petId);
        
        List<PetAchievement> achievements = petAchievementRepository
                .findByPetIdAndStatusOrderByCompletedAtDesc(petId, Status.COMPLETADO);
        
        log.info("Mascota {} tiene {} logros completados", petId, achievements.size());
        return PetAchievementMapper.toResponseDTOList(achievements);
    }

    //Obtener logros EN PROGRESO de una mascota
    @Transactional(readOnly = true)
    public List<PetAchievementResponseDTO> getInProgressAchievements(UUID petId) {
        
        // Validar que la mascota exista
        petService.findById(petId);
        
        List<PetAchievement> achievements = petAchievementRepository
                .findByPetIdAndStatus(petId, Status.EN_PROGRESO);
        
        log.info("Mascota {} tiene {} logros en progreso", petId, achievements.size());
        return PetAchievementMapper.toResponseDTOList(achievements);
    }

    // Obtener un logro específico de una mascota
    @Transactional(readOnly = true)
    public PetAchievementResponseDTO getById(UUID id) {
        log.debug("Obteniendo logro de mascota con ID {}", id);
        
        PetAchievement petAchievement = petAchievementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Logro de mascota no encontrado con ID: " + id));
        
        return PetAchievementMapper.toResponseDTO(petAchievement);
    }

    //Contar logros completados de una mascota
    @Transactional(readOnly = true)
    public long countCompletedAchievements(UUID petId) {
        petService.findById(petId);
        
        return petAchievementRepository
                .findByPetIdAndStatusOrderByCompletedAtDesc(petId, Status.COMPLETADO)
                .size();
    }
}