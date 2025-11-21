package com.api.modules.achievement.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.common.enums.AchievementType;
import com.api.common.enums.NotificationChannel;
import com.api.common.enums.NotificationType;
import com.api.common.enums.Status;
import com.api.common.enums.ValidationPeriod;
import com.api.modules.achievement.model.Achievement;
import com.api.modules.achievementRequirement.model.AchievementRequirement;
import com.api.modules.achievementRequirement.repository.AchievementRequirementRepository;
import com.api.modules.challenge.model.Challenge;
import com.api.modules.notification.service.NotificationService;
import com.api.modules.pet.model.Pet;
import com.api.modules.petAchievement.model.PetAchievement;
import com.api.modules.petAchievement.repository.PetAchievementRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AchievementValidationService {

    private final AchievementRequirementRepository requirementRepository;
    private final PetAchievementRepository petAchievementRepository;
    private final AchievementCalculationService calculationService;
    private final NotificationService notificationService;

    /**
     * Método principal que se llama desde PetChallengeService
     * cuando una mascota completa un reto
     */
    @Transactional
    public void checkAndGrantAchievements(Pet pet, Challenge completedChallenge) {
        log.info("Verificando logros para mascota '{}' tras completar reto '{}'",
                pet.getNombre(), completedChallenge.getName());

        // Buscar todos los requisitos que involucran este reto
        List<AchievementRequirement> requirements = requirementRepository
                .findByChallengeIdAndAchievementStatusAndAchievementAchievementType(
                        completedChallenge.getId(),
                        Status.ACTIVO,
                        AchievementType.MASCOTA_RETO);

        if (requirements.isEmpty()) {
            log.info("No hay logros asociados al reto '{}'", completedChallenge.getName());
            return;
        }

        log.info(" Encontrados {} requisitos relacionados con el reto", requirements.size());

        // Agrupar requisitos por logro y validar cada uno
        requirements.stream()
                .map(AchievementRequirement::getAchievement)
                .distinct() // Evitar procesar el mismo logro múltiples veces
                .forEach(achievement -> validateAndGrantAchievement(pet, achievement));
    }

    // Valida si la mascota cumple con TODOS los requisitos de un logro

    private void validateAndGrantAchievement(Pet pet, Achievement achievement) {
        log.info("Validando logro '{}' para mascota '{}'",
                achievement.getName(), pet.getNombre());

        // Obtener todos los requisitos del logro (solo con retos activos)
        List<AchievementRequirement> requirements = requirementRepository
                .findByAchievementId(achievement.getId())
                .stream()
                .filter(req -> req.getChallenge().getStatus() == Status.ACTIVO)
                .toList();

        if (requirements.isEmpty()) {
            log.warn("El logro '{}' no tiene requisitos activos", achievement.getName());
            return;
        }

        // Determinar el periodo según el primer requisito (todos deberían tener el
        // nmismo)
        ValidationPeriod period = requirements.get(0).getValidationPeriod();
        LocalDate periodStart = calculationService.calculatePeriodStart(period);
        LocalDate periodEnd = calculationService.calculatePeriodEnd(period);

        log.info("Periodo de validación: {} (desde {} hasta {})",
                period, periodStart, periodEnd);

        // Verificar si ya obtuvo este logro
        if (alreadyObtained(pet.getId(), achievement, periodStart)) {
            log.info("La mascota ya obtuvo este logro");
            return;
        }

        // Validar cada requisito
        boolean allRequirementsMet = true;
        for (AchievementRequirement req : requirements) {
            int completedCount = calculationService.countChallengeCompletions(
                    pet.getId(),
                    req.getChallenge().getId(),
                    periodStart,
                    periodEnd,
                    achievement.getCreatedAt(),
                    achievement.getCountFromCreation());

            log.info("   ➤ Requisito '{}': {}/{} completados",
                    req.getChallenge().getName(),
                    completedCount,
                    req.getRepetitions());

            if (completedCount < req.getRepetitions()) {
                allRequirementsMet = false;

                // Actualizar o crear registro de progreso
                updateOrCreatePetAchievement(pet, achievement, periodStart, periodEnd, Status.EN_PROGRESO);
                break;
            }
        }

        // Si cumplió todos los requisitos, otorgar el logro
        if (allRequirementsMet) {
            grantAchievement(pet, achievement, periodStart, periodEnd);
        }
    }

    // Verifica si la mascota ya obtuvo el logro
    private boolean alreadyObtained(UUID petId, Achievement achievement, LocalDate periodStart) {
        if (!achievement.getRepeatable()) {
            // Logro único: verificar si ya lo obtuvo alguna vez
            boolean obtained = petAchievementRepository.existsByPetIdAndAchievementIdAndStatus(
                    petId,
                    achievement.getId(),
                    Status.COMPLETADO);

            if (obtained) {
                log.info("Logro único '{}' ya fue obtenido anteriormente", achievement.getName());
            }

            return obtained;
        } else {
            // Logro repetible: verificar solo en este periodo
            if (periodStart != null) {
                boolean obtained = petAchievementRepository
                        .existsByPetIdAndAchievementIdAndPeriodStartAndStatus(
                                petId,
                                achievement.getId(),
                                periodStart,
                                Status.COMPLETADO);

                if (obtained) {
                    log.info("Logro repetible '{}' ya fue obtenido en este periodo",
                            achievement.getName());
                }

                return obtained;
            }
        }

        return false;
    }

    // Otorga el logro a la mascota
    private void grantAchievement(Pet pet, Achievement achievement,
            LocalDate periodStart, LocalDate periodEnd) {
        log.info("¡Mascota '{}' obtuvo el logro '{}'!", pet.getNombre(), achievement.getName());

        // Buscar o crear PetAchievement
        Optional<PetAchievement> existingOpt = petAchievementRepository
                .findByPetIdAndAchievementIdAndPeriodStart(
                        pet.getId(),
                        achievement.getId(),
                        periodStart);

        PetAchievement petAchievement;
        if (existingOpt.isPresent()) {
            petAchievement = existingOpt.get();
        } else {
            petAchievement = new PetAchievement();
            petAchievement.setPet(pet);
            petAchievement.setAchievement(achievement);
            petAchievement.setPeriodStart(periodStart);
            petAchievement.setPeriodEnd(periodEnd);
        }

        petAchievement.setStatus(Status.COMPLETADO);
        petAchievement.setCompletedAt(LocalDateTime.now());

        petAchievementRepository.save(petAchievement);

        notificationService.createNotificationForUser(
                pet.getUser().getId(),
                "¡Has obtenido un nuevo logro!",
                String.format("¡Tu mascota '%s' ha obtenido el logro '%s'! ¡Felicidades!",
                        pet.getNombre(), achievement.getName()),
                NotificationType.LOGRO,
                NotificationChannel.BOTH,
                "/logros");
    }

    // Actualiza o crea un registro de PetAchievement en progreso
    private void updateOrCreatePetAchievement(Pet pet, Achievement achievement,
            LocalDate periodStart, LocalDate periodEnd,
            Status status) {
        Optional<PetAchievement> existingOpt = petAchievementRepository
                .findByPetIdAndAchievementIdAndPeriodStart(
                        pet.getId(),
                        achievement.getId(),
                        periodStart);

        if (existingOpt.isEmpty()) {
            // Crear nuevo registro en progreso
            PetAchievement petAchievement = new PetAchievement();
            petAchievement.setPet(pet);
            petAchievement.setAchievement(achievement);
            petAchievement.setPeriodStart(periodStart);
            petAchievement.setPeriodEnd(periodEnd);
            petAchievement.setStatus(status);

            petAchievementRepository.save(petAchievement);

            log.info("Creado registro de logro en progreso para '{}'", achievement.getName());
        }
        // Si ya existe, no hacemos nada (el progreso se calcula on-demand)
    }

}