package com.api.modules.userAchievement.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.modules.userAchievement.model.UserAchievement;

public interface UserAchievementRepository extends JpaRepository<UserAchievement, UUID> {

    // buscar si un usuario ya tiene un logro especifico
    Optional<UserAchievement> findByUserIdAndAchievementId(UUID userId, UUID achievementId);

    // obtener todos los logros de un usuario
    List<UserAchievement> findByUserId(UUID userId);

    // contar cuantos logros ha completado un usuario
    Long countByUserId(UUID userId);

    // verificar existe un logro para un usuario
    boolean existsByUserIdAndAchievementId(UUID userId, UUID achievementId);

}
