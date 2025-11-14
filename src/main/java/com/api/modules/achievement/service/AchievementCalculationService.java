package com.api.modules.achievement.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.common.enums.ValidationPeriod;
import com.api.modules.petchallenge.repository.PetChallengeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//Servicio utilitario para cálculos relacionados con logros

@Service
@RequiredArgsConstructor
@Slf4j
public class AchievementCalculationService {

    private final PetChallengeRepository petChallengeRepository;

    // Cuenta cuántas veces una mascota completó un reto en un periodo

    @Transactional(readOnly = true)
    public int countChallengeCompletions(UUID petId, UUID challengeId,
            LocalDate periodStart, LocalDate periodEnd,
            LocalDateTime achievementCreatedAt,
            Boolean countFromCreation) {
        if (periodStart == null) {
            // TOTAL - Verificar si debe contar desde la creación del logro
            if (countFromCreation != null && countFromCreation) {
                // Solo contar retos completados DESPUÉS de crear el logro
               log.info(" Contando desde creación del logro: {}", achievementCreatedAt);
                return (int) petChallengeRepository.countByPetIdAndChallengeIdAndCreatedAtAfter(
                        petId, challengeId, achievementCreatedAt);
            } else {
                // Contar todos los retos históricos
              
                return (int) petChallengeRepository.countByPetIdAndChallengeId(petId, challengeId);
            }
        } else {
            // SEMANAL o MENSUAL - contar en el periodo
            LocalDateTime startDateTime = periodStart.atStartOfDay();
            LocalDateTime endDateTime = periodEnd.atTime(23, 59, 59);

            return (int) petChallengeRepository.countByPetIdAndChallengeIdAndCreatedAtBetween(
                    petId, challengeId, startDateTime, endDateTime);
        }
    }

    //Calcula el inicio del periodo según ValidationPeriod
    public LocalDate calculatePeriodStart(ValidationPeriod period) {
        return switch (period) {
            case TOTAL -> null;
            case SEMANAL -> LocalDate.now().with(DayOfWeek.MONDAY);
            case MENSUAL -> LocalDate.now().withDayOfMonth(1);
        };
    }

    //Calcula el fin del periodo según ValidationPeriod
    public LocalDate calculatePeriodEnd(ValidationPeriod period) {
        return switch (period) {
            case TOTAL -> null;
            case SEMANAL -> LocalDate.now().with(DayOfWeek.SUNDAY);
            case MENSUAL -> LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        };
    }
}