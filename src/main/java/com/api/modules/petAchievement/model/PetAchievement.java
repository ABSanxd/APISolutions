package com.api.modules.petAchievement.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.api.common.enums.Status;
import com.api.modules.achievement.model.Achievement;
import com.api.modules.pet.model.Pet;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "pet_achievement")

public class PetAchievement {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "achievement_id", nullable = false)
    private Achievement achievement;

    // Para logros repetibles: identificar el periodo/fecha específica
    private LocalDate periodStart; // null si es TOTAL
    private LocalDate periodEnd; // null si es TOTAL

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status; // EN_PROGRESO, COMPLETADO, EXPIRADO

    private LocalDateTime completedAt; // Fecha de completado, null si no lo completó

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // cuando empezó a trabajar para obtener el logro

}
