package com.api.modules.achievement.service;

import java.util.List;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.api.common.enums.AchievementType;
import com.api.common.enums.Status;
import com.api.modules.achievement.dto.AchievementCreateDTO;
import com.api.modules.achievement.dto.AchievementResponseDTO;
import com.api.modules.achievement.dto.AchievementUpdateDTO;
import com.api.modules.achievement.mapper.AchievementMapper;
import com.api.modules.achievement.model.Achievement;
import com.api.modules.achievement.repository.AchievementRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AchievementService {

    private final AchievementRepository achievementRepository;

    // Crear logro
    public AchievementResponseDTO createAchievement(AchievementCreateDTO dto) {
        // Validar que no exista un logro con el mismo nombre
        if (achievementRepository.existsByName(dto.getName())) {
            throw new DataIntegrityViolationException("Ya existe un logro con ese nombre");
        }

        // Validar que si es logro de usuario, tenga puntos
        if (dto.getAchievementType() != AchievementType.MASCOTA_RETO && dto.getPoints() == null) {
            throw new IllegalArgumentException("Los logros de usuario deben tener puntos asignados");
        }

        // Validar que si es logro de mascota, NO tenga puntos
        if (dto.getAchievementType() == AchievementType.MASCOTA_RETO && dto.getPoints() != null) {
            throw new IllegalArgumentException("Los logros de mascota no deben tener puntos");
        }

        Achievement achievement = AchievementMapper.toEntity(dto);
        Achievement savedAchievement = achievementRepository.save(achievement);

        return AchievementMapper.toResponseDTO(savedAchievement);
    }

    // Obtener todos los logros con filtros estado y tipo
    public List<AchievementResponseDTO> getAchievements(Status status, AchievementType type) {
        List<Achievement> achievements;

        // Filtrar por tipo y estado
        if (type != null && status != null) {
            achievements = achievementRepository.findByAchievementTypeAndStatusOrderByCreatedAtDesc(type, status);
        }
        // Filtrar solo por tipo (todos los estados)
        else if (type != null) {
            achievements = achievementRepository.findByAchievementTypeOrderByCreatedAtDesc(type);
        }
        // Filtrar solo por estado (todos los tipos)
        else if (status != null) {
            achievements = achievementRepository.findByStatusOrderByCreatedAtDesc(status);
        }
        // Sin filtros (todos los logros)
        else {
            achievements = achievementRepository.findAll();
        }

        return AchievementMapper.toResponseDTOList(achievements);
    }

    // Obtener logro por ID
    public AchievementResponseDTO getAchievementById(UUID id) {
        return achievementRepository.findById(id)
                .map(AchievementMapper::toResponseDTO)
                .orElseThrow(() -> new RuntimeException("Logro no encontrado"));
    }

    // Actualizar logro
    public AchievementResponseDTO updateAchievement(UUID id, AchievementUpdateDTO dto) {
        return achievementRepository.findById(id)
                .map(achievement -> {
                    // Validar cambio de tipo y puntos
                    if (dto.getAchievementType() != null && dto.getPoints() != null) {
                        if (dto.getAchievementType() == AchievementType.MASCOTA_RETO && dto.getPoints() != null) {
                            throw new IllegalArgumentException("Los logros de mascota no deben tener puntos");
                        }
                    }
                    AchievementMapper.updateEntity(achievement, dto);
                    Achievement updatedAchievement = achievementRepository.save(achievement);

                    return AchievementMapper.toResponseDTO(updatedAchievement);
                })
                .orElseThrow(() -> new RuntimeException("Logro no encontrado para actualizar"));
    }

    // Inactivar logro
    public void deactivateAchievement(UUID id) {
        Achievement achievement = achievementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Logro no encontrado para inactivar"));

        achievement.setStatus(Status.INACTIVO);
        achievementRepository.save(achievement);
    }

    // Activar logro
    public void activateAchievement(UUID id) {
        Achievement achievement = achievementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Logro no encontrado para activar"));

        achievement.setStatus(Status.ACTIVO);
        achievementRepository.save(achievement);
    }

    // --------------------------------
    /*
     * private void checkAndAwardAchievements(UUID petId, UUID completedChallengeId)
     * {
     * 
     * // Obtener todos los requisitos que usan este reto.
     * List<ChallengeRequirements> requirements =
     * requirementsRepository.findByChallengeId(completedChallengeId);
     * 
     * // Agrupar por el ID de Logro al que pertenecen.
     * requirements.stream()
     * .collect(Collectors.groupingBy(ChallengeRequirements::getAchievementId))
     * .forEach((achievementId, requirementList) -> {
     * 
     * // Para este Logro "achievementId", verifica si todos sus requisitos se
     * cumplen.
     * boolean isAchievementComplete = requirementList.stream().allMatch(req -> {
     * 
     * // Contar cuántas veces la mascota ha completado el reto requerido.
     * long count = petChallengeRepository.countByPetIdAndChallengeId(
     * petId, req.getChallenge().getId()); // método countByPetIdAndChallengeId
     * 
     * // Compara si el conteo actual >= repeticiones requeridas.
     * return count >= req.getRepetitions();
     * });
     * 
     * if (isAchievementComplete) {
     * // Lógica para otorgar el logro a la mascota.
     * System.out.println(
     * "LOGRO DESBLOQUEADO (ID temporal): " + achievementId + " para la mascota: " +
     * petId);
     * }
     * });
     * }
     */

}
