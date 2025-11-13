package com.api.modules.petAchievement.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.common.enums.AchievementType;
import com.api.common.enums.Status;
import com.api.common.enums.ValidationPeriod;
import com.api.common.exception.ResourceNotFoundException;
import com.api.modules.achievement.model.Achievement;
import com.api.modules.achievement.repository.AchievementRepository;
import com.api.modules.achievementRequirement.model.AchievementRequirement;
import com.api.modules.achievementRequirement.repository.AchievementRequirementRepository;
import com.api.modules.petAchievement.dto.AchievementProgressDTO;
import com.api.modules.petAchievement.dto.RequirementProgressDTO;
import com.api.modules.petAchievement.repository.PetAchievementRepository;
import com.api.modules.pet.service.PetService;
import com.api.modules.petchallenge.repository.PetChallengeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PetAchievementProgressService {

    private final AchievementRepository achievementRepository;
    private final AchievementRequirementRepository requirementRepository;
    private final PetChallengeRepository petChallengeRepository;
    private final PetAchievementRepository petAchievementRepository;
    private final PetService petService;

    /**
     * Obtener el progreso de un logro específico para una mascota
     */
    @Transactional(readOnly = true)
    public AchievementProgressDTO getAchievementProgress(UUID petId, UUID achievementId) {

        // Validar que la mascota exista
        petService.findById(petId);

        // Validar que el logro exista
        Achievement achievement = achievementRepository.findById(achievementId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Logro no encontrado con ID: " + achievementId));

        // Obtener requisitos activos del logro
        List<AchievementRequirement> requirements = requirementRepository
                .findByAchievementId(achievementId)
                .stream()
                .filter(req -> req.getChallenge().getStatus() == Status.ACTIVO)
                .toList();

        if (requirements.isEmpty()) {
            throw new IllegalStateException(
                    "El logro '" + achievement.getName() + "' no tiene requisitos activos");
        }

        // Determinar el periodo
        ValidationPeriod period = requirements.get(0).getValidationPeriod();
        LocalDate periodStart = calculatePeriodStart(period);
        LocalDate periodEnd = calculatePeriodEnd(period);

        // Verificar si ya está completado
        boolean completed = petAchievementRepository
                .existsByPetIdAndAchievementIdAndPeriodStartAndStatus(
                        petId, achievementId, periodStart, Status.COMPLETADO);

        // Calcular progreso de cada requisito
        List<RequirementProgressDTO> requirementProgress = new ArrayList<>();

        for (AchievementRequirement req : requirements) {
            int currentCount = countChallengeCompletions(
                    petId,
                    req.getChallenge().getId(),
                    periodStart,
                    periodEnd);

            RequirementProgressDTO reqDto = new RequirementProgressDTO(
                    req.getId(),
                    req.getChallenge().getId(),
                    req.getChallenge().getName(),
                    req.getChallenge().getCategory().toString(),
                    currentCount,
                    req.getRepetitions(),
                    (currentCount * 100.0) / req.getRepetitions(),
                    currentCount >= req.getRepetitions());

            requirementProgress.add(reqDto);
        }

        return new AchievementProgressDTO(
                achievement.getId(),
                achievement.getName(),
                achievement.getDescription(),
                achievement.getPhrase(),
                // achievement.getPoints(),
                achievement.getRepeatable(),
                completed,
                periodStart,
                periodEnd,
                period.toString(),
                requirementProgress);
    }

    // Obtener el progreso de TODOS los logros disponibles para una mascota
    @Transactional(readOnly = true)
    public List<AchievementProgressDTO> getAllAchievementsProgress(UUID petId) {
        log.debug("Calculando progreso de todos los logros para mascota {}", petId);

        // Validar que la mascota exista
        petService.findById(petId);

        // Obtener todos los logros activos de tipo RETO_MASCOTA
        List<Achievement> achievements = achievementRepository
                .findByStatusAndAchievementType(Status.ACTIVO, AchievementType.MASCOTA_RETO);

        List<AchievementProgressDTO> progressList = new ArrayList<>();

        for (Achievement achievement : achievements) {
            try {
                AchievementProgressDTO progress = getAchievementProgress(petId, achievement.getId());
                progressList.add(progress);
            } catch (Exception e) {
                log.warn("No se pudo calcular progreso del logro {}: {}",
                        achievement.getName(), e.getMessage());
            }
        }

        return progressList;
    }

    // Cuenta cuántas veces una mascota completó un reto en un periodo
    private int countChallengeCompletions(UUID petId, UUID challengeId,
            LocalDate start, LocalDate end) {
        if (start == null) {
            // TOTAL - contar todos los registros históricos
            return (int) petChallengeRepository.countByPetIdAndChallengeId(petId, challengeId);
        } else {
            // SEMANAL o MENSUAL - contar en el periodo
            LocalDateTime startDateTime = start.atStartOfDay();
            LocalDateTime endDateTime = end.atTime(23, 59, 59);

            return (int) petChallengeRepository.countByPetIdAndChallengeIdAndCreatedAtBetween(
                    petId, challengeId, startDateTime, endDateTime);
        }
    }

    //Calcula el inicio del periodo según ValidationPeriod
    private LocalDate calculatePeriodStart(ValidationPeriod period) {
        return switch (period) {
            case TOTAL -> null;
            case SEMANAL -> LocalDate.now().with(DayOfWeek.MONDAY);
            case MENSUAL -> LocalDate.now().withDayOfMonth(1);
        };
    }

    //Calcula el fin del periodo según ValidationPeriod
    private LocalDate calculatePeriodEnd(ValidationPeriod period) {
        return switch (period) {
            case TOTAL -> null;
            case SEMANAL -> LocalDate.now().with(DayOfWeek.SUNDAY);
            case MENSUAL -> LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        };
    }
}