package com.api.modules.achievementRequirement.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.common.enums.AchievementType;
import com.api.common.enums.Status;
import com.api.modules.achievementRequirement.model.AchievementRequirement;

@Repository
public interface AchievementRequirementRepository extends JpaRepository<AchievementRequirement, UUID> {
    
    // Obtener todos los requisitos de un logro
    List<AchievementRequirement> findByAchievementId(UUID achievementId);
    
    // Buscar requisitos por reto (para validación de logros) o sea en que requisitos de un logro se encuentra el reto
    List<AchievementRequirement> findByChallengeIdAndAchievementStatusAndAchievementAchievementType(
        UUID challengeId,
        Status status,
        AchievementType achievementType
    );
    
    // Verificar si existe un requisito para evitar duplicados
    boolean existsByAchievementIdAndChallengeId(UUID achievementId, UUID challengeId);
}