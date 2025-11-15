package com.api.modules.achievement.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.common.enums.AchievementType;
import com.api.common.enums.Status;
import com.api.modules.achievement.model.Achievement;

public interface AchievementRepository extends JpaRepository<Achievement, UUID>{

    // Obtener logros por estado
    List<Achievement> findByStatusOrderByCreatedAtDesc(Status status);

    //logros por tipo
     List<Achievement> findByAchievementTypeOrderByCreatedAtDesc(AchievementType achievementType);

    // Obtener logros por tipo y estado
    List<Achievement> findByAchievementTypeAndStatusOrderByCreatedAtDesc(
        AchievementType achievementType, 
        Status status
    );

    // Buscar por nombre
    boolean existsByName(String name);

     List<Achievement> findByStatusAndAchievementType(Status status, AchievementType achievementType);
}


