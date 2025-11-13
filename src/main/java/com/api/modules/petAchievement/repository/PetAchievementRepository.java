package com.api.modules.petAchievement.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.common.enums.Status;
import com.api.modules.petAchievement.model.PetAchievement;

public interface PetAchievementRepository extends JpaRepository<PetAchievement, UUID> {

    // Verificar si ya obtuvo un logro único
    boolean existsByPetIdAndAchievementIdAndStatus(UUID petId, UUID achievementId, Status status);

    // Verificar si ya obtuvo un logro repetible en un periodo específico
    boolean existsByPetIdAndAchievementIdAndPeriodStartAndStatus(
            UUID petId,
            UUID achievementId,
            LocalDate periodStart,
            Status status);

    // Buscar PetAchievement para actualizar
    Optional<PetAchievement> findByPetIdAndAchievementIdAndPeriodStart(
            UUID petId,
            UUID achievementId,
            LocalDate periodStart);

    // Obtener logros completados de una mascota
    List<PetAchievement> findByPetIdAndStatusOrderByCompletedAtDesc(UUID petId, Status status);

    // Obtener logros en progreso de una mascota
    List<PetAchievement> findByPetIdAndStatus(UUID petId, Status status);

}
