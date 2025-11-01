package com.api.modules.challengerequirements.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.api.modules.challengerequirements.model.ChallengeRequirements;
import java.util.UUID;
import java.util.List;
public interface ChallengeRequirementsRepository extends JpaRepository<ChallengeRequirements, UUID> {
    // Obtener todos los requisitos que involucran a un desafío específico
    List<ChallengeRequirements> findByChallengeId(UUID challengeId);

    //obtener todos los requisitos para un logro especifico
    List<ChallengeRequirements> findByAchievementId(UUID achievementId);
    
}
