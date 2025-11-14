package com.api.modules.achievementRequirement.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.common.enums.Status;
import com.api.common.exception.ResourceNotFoundException;
import com.api.modules.achievement.model.Achievement;
import com.api.modules.achievement.repository.AchievementRepository;
import com.api.modules.achievementRequirement.dto.AchievementRequirementCreateDTO;
import com.api.modules.achievementRequirement.dto.AchievementRequirementResponseDTO;
import com.api.modules.achievementRequirement.dto.AchievementRequirementUpdateDTO;
import com.api.modules.achievementRequirement.mapper.AchievementRequirementMapper;
import com.api.modules.achievementRequirement.model.AchievementRequirement;
import com.api.modules.achievementRequirement.repository.AchievementRequirementRepository;
import com.api.modules.challenge.model.Challenge;
import com.api.modules.challenge.repository.ChallengeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AchievementRequirementService {

    private final AchievementRequirementRepository requirementRepository;
    private final AchievementRepository achievementRepository;
    private final ChallengeRepository challengeRepository;

    /**
     * Crear un nuevo requisito para un logro
     */
    @Transactional
    public AchievementRequirementResponseDTO create(AchievementRequirementCreateDTO dto) {
        log.info("Creando requisito para logro {} con reto {}", dto.achievementId(), dto.challengeId());

        // Validar que el logro exista
        Achievement achievement = achievementRepository.findById(dto.achievementId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Logro no encontrado con ID: " + dto.achievementId()));

        // Validar que el logro esté activo
        if (achievement.getStatus() != Status.ACTIVO) {
            throw new IllegalArgumentException(
                    "No se puede crear un requisito para el logro '" + achievement.getName() +
                            "' porque está inactivo");
        }

        // Validar que el reto exista
        Challenge challenge = challengeRepository.findById(dto.challengeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Reto no encontrado con ID: " + dto.challengeId()));

        // Validar que el reto esté activo
        if (challenge.getStatus() != Status.ACTIVO) {
            throw new IllegalArgumentException(
                    "No se puede crear un requisito con el reto '" + challenge.getName() +
                            "' porque está inactivo");
        }

        // Validar que no exista ya un requisito con el mismo logro y reto
        if (requirementRepository.existsByAchievementIdAndChallengeId(
                dto.achievementId(), dto.challengeId())) {
            throw new IllegalArgumentException(
                    "Ya existe un requisito para este logro con el reto '" + challenge.getName() + "'");
        }

        // Crear y guardar el requisito
        AchievementRequirement requirement = AchievementRequirementMapper.toEntity(dto, achievement, challenge);
        AchievementRequirement saved = requirementRepository.save(requirement);

        log.info("Requisito creado exitosamente con ID: {}", saved.getId());
        return AchievementRequirementMapper.toResponseDTO(saved);
    }

    // Obtener todos los requisitos de un logro

    @Transactional(readOnly = true)
    public List<AchievementRequirementResponseDTO> getByAchievementId(UUID achievementId) {

        // Validar que el logro exista
        if (!achievementRepository.existsById(achievementId)) {
            throw new ResourceNotFoundException("Logro no encontrado con ID: " + achievementId);
        }

        List<AchievementRequirement> requirements = requirementRepository.findByAchievementId(achievementId);
        return AchievementRequirementMapper.toResponseDTOList(requirements);
    }

    // Obtener un requisito por ID

    @Transactional(readOnly = true)
    public AchievementRequirementResponseDTO getById(UUID id) {

        AchievementRequirement requirement = requirementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Requisito no encontrado con ID: " + id));

        return AchievementRequirementMapper.toResponseDTO(requirement);
    }

    // Actualizar un requisito
    @Transactional
    public AchievementRequirementResponseDTO update(UUID id, AchievementRequirementUpdateDTO dto) {

        // Buscar el requisito
        AchievementRequirement requirement = requirementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Requisito no encontrado con ID: " + id));

        // Si se va a cambiar el reto, validar que exista y que no haya duplicado
        Challenge challenge = null;
        if (dto.challengeId() != null) {
            challenge = challengeRepository.findById(dto.challengeId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Reto no encontrado con ID: " + dto.challengeId()));
            // Validar que el reto esté activo
            if (challenge.getStatus() != Status.ACTIVO) {
                throw new IllegalArgumentException(
                        "No se puede asociar el reto '" + challenge.getName() +
                                "' porque está inactivo");
            }

            // Validar que no exista otro requisito con el mismo logro y el nuevo reto, o
            // sea no retos repetidos
            if (!dto.challengeId().equals(requirement.getChallenge().getId()) &&
                    requirementRepository.existsByAchievementIdAndChallengeId(
                            requirement.getAchievement().getId(), dto.challengeId())) {
                throw new IllegalArgumentException(
                        "Ya existe un requisito para este logro con el reto '" + challenge.getName() + "'");
            }
        }

        // Actualizar el requisito
        AchievementRequirementMapper.updateEntity(requirement, dto, challenge);
        AchievementRequirement updated = requirementRepository.save(requirement);

        log.info("Requisito actualizado exitosamente");
        return AchievementRequirementMapper.toResponseDTO(updated);
    }

    // Eliminar un requisito
    @Transactional
    public void delete(UUID id) {

        if (!requirementRepository.existsById(id)) {
            throw new ResourceNotFoundException("Requisito no encontrado con ID: " + id);
        }

        requirementRepository.deleteById(id);
        log.info("Requisito eliminado exitosamente");
    }

    // Obtener todos los requisitos
    @Transactional(readOnly = true)
    public List<AchievementRequirementResponseDTO> getAll() {
        log.debug("Obteniendo todos los requisitos");

        List<AchievementRequirement> requirements = requirementRepository.findAll();
        return AchievementRequirementMapper.toResponseDTOList(requirements);
    }
}