package com.api.modules.petchallenge.service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.api.common.enums.Frequency;
import com.api.common.enums.Status;
import com.api.modules.achievement.service.AchievementValidationService;
import com.api.modules.challenge.model.Challenge;
import com.api.modules.challenge.service.ChallengeService;
import com.api.modules.pet.model.Pet;
import com.api.modules.pet.service.PetService;
import com.api.modules.petchallenge.dto.PetChallengeCreateDTO;
import com.api.modules.petchallenge.dto.PetChallengeResponseDTO;
import com.api.modules.petchallenge.mapper.PetChallengeMapper;
import com.api.modules.petchallenge.models.PetChallenge;
import com.api.modules.petchallenge.repository.PetChallengeRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PetChallengeService {

    private final PetChallengeRepository petChallengeRepository;
    private final PetService petService;
    private final ChallengeService challengeService;
    private final AchievementValidationService achievementValidationService;

    // Marcar reto como completado
    @Transactional
    public PetChallengeResponseDTO completeChallenge(UUID petId, PetChallengeCreateDTO dto) {

        Pet pet = petService.findById(petId);

        Challenge challenge = challengeService.findChallengeEntityById(dto.challengeId());

        if (challenge.getStatus() != Status.ACTIVO) {
            throw new RuntimeException(
                    "El reto '" + challenge.getName() + "' no está activo y no puede ser completado.");
        }

        if(alreadyCompletedInPeriod(petId, challenge)){
            throw new IllegalStateException(
                "Ya completaste el reto " + challenge.getName() +" " + getPeriodMessage(challenge.getFrequency()) + "." 
            );
        }

        // Crear RetoMascota
        PetChallenge petChallenge = new PetChallenge();
        petChallenge.setPet(pet);
        petChallenge.setChallenge(challenge);
        PetChallenge savedPetChallenge = petChallengeRepository.save(petChallenge);

        // Sumar XP
        pet.setPetXp(pet.getPetXp() + challenge.getPoints());
        // Verificar Nivel
        petService.checkAndLevelUp(pet);
        // guardamos desde aqui para que le añada los XP
        petService.save(pet);

        // DISPARAR validación de logros
        achievementValidationService.checkAndGrantAchievements(pet, challenge);

        return PetChallengeMapper.toResponseDTO(savedPetChallenge);
    }

    private boolean alreadyCompletedInPeriod(UUID petId, Challenge challenge){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime periodStart;

        switch (challenge.getFrequency()) {
            case DIARIO:
                periodStart = now.truncatedTo(ChronoUnit.DAYS);
                break;
                
            case SEMANAL:
                periodStart = now.with(DayOfWeek.MONDAY).truncatedTo(ChronoUnit.DAYS);
                break;
                
            case MENSUAL:
                periodStart = now.withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS);
                break;
                
            default:
                return false;
        }

        return petChallengeRepository.existsByPetIdAndChallengeIdAndCreatedAtAfter(
                petId,
                challenge.getId(),
                periodStart
        );
    }

    private String getPeriodMessage(Frequency frequency) {
        return switch (frequency) {
            case DIARIO -> "hoy";
            case SEMANAL -> "esta semana";
            case MENSUAL -> "este mes";
        };
    }

    // CONSULTAS
    // Historial de retos completados por una mascota, ordenados por fecha
    public List<PetChallengeResponseDTO> getPetChallengeHistory(UUID petId) {
        petService.findById(petId);
        List<PetChallenge> petChallenges = petChallengeRepository.findByPetIdOrderByCreatedAtDesc(petId);

        return PetChallengeMapper.toResponseDTOList(petChallenges);
    }

    // retos completados por una mascota - HOY
    public List<PetChallengeResponseDTO> getPetChallengesToday(UUID petId) {
        petService.findById(petId);

        LocalDateTime startOfDay = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1); // Hasta el final del día

        List<PetChallenge> petChallenges = petChallengeRepository
                .findByPetIdAndCreatedAtBetween(petId, startOfDay, endOfDay);

        return PetChallengeMapper.toResponseDTOList(petChallenges);
    }

    // Obtener retos completados esta SEMANA
    public List<PetChallengeResponseDTO> getPetChallengesThisWeek(UUID petId) {
        LocalDateTime startOfWeek = LocalDateTime.now().minusDays(7);
        LocalDateTime now = LocalDateTime.now();

        List<PetChallenge> petChallenges = petChallengeRepository
                .findByPetIdAndCreatedAtBetween(petId, startOfWeek, now);

        return PetChallengeMapper.toResponseDTOList(petChallenges);
    }

}
