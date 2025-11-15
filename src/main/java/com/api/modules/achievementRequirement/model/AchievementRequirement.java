package com.api.modules.achievementRequirement.model;

import com.api.common.enums.ValidationPeriod;
import com.api.modules.achievement.model.Achievement;
import com.api.modules.challenge.model.Challenge;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Entity
@Table(name = "achievement_requirements")

public class AchievementRequirement {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "achievement_id", nullable = false)
    private Achievement achievement;
    // puede ser el mismo cuando un logro tiene varios requisitos de reto

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge; // reto, el cual puede ser diferente en cada registro si pertenece a un logrocon
                                 // varios req/retos

    @Column(nullable = false)
    private Integer repetitions;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ValidationPeriod validationPeriod; // SEMANAL, MENSUAL, TOTAL

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

}
