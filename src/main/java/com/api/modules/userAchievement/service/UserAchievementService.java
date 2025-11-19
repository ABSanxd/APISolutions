package com.api.modules.userAchievement.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.common.enums.AchievementType;
import com.api.common.enums.Status;
import com.api.modules.achievement.model.Achievement;
import com.api.modules.achievement.repository.AchievementRepository;
import com.api.modules.adoption.repository.AdoptionRequestRepository;
import com.api.modules.notification.service.NotificationService;
import com.api.modules.publication.repository.PublicationRepository;
import com.api.modules.user.model.User;
import com.api.modules.user.repository.UserRepository;
import com.api.modules.userAchievement.dto.UserAchievementResponseDTO;
import com.api.modules.userAchievement.mapper.UserAchievementMapper;
import com.api.modules.userAchievement.model.UserAchievement;
import com.api.modules.userAchievement.repository.UserAchievementRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAchievementService {

    private final UserAchievementRepository userAchievementRepository;
    private final AchievementRepository achievementRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final AdoptionRequestRepository adoptionRequestRepository;
    private final PublicationRepository publicationRepository;

    /**
     * Otorgar logros de adopción
     */
    @Transactional
    public void grantAdoptionAchievements(UUID adopterId, UUID rescuerId, String petName) {
        log.info("🏆 Otorgando logros de adopción - Adoptante: {}, Rescatista: {}", adopterId, rescuerId);

        // Otorgar logro al ADOPTANTE
        grantAchievementByType(adopterId, AchievementType.USUARIO_ADOPTANTE, petName);

        // Otorgar logro al RESCATISTA
        grantAchievementByType(rescuerId, AchievementType.USUARIO_RESCATISTA, petName);
    }

    /**
     * Otorgar logro por tipo - LÓGICA CORREGIDA
     */
    @Transactional
    public void grantAchievementByType(UUID userId, AchievementType type, String petName) {

        // 1. Obtener el conteo REAL de adopciones completadas
        long countCompleted = getCompletedCount(userId, type);

        log.info("📊 Usuario: {} - Tipo: {} - Adopciones completadas: {}", userId, type, countCompleted);

        // 2. Buscar todos los logros activos de este tipo
        List<Achievement> achievements = achievementRepository
                .findByStatusAndAchievementType(Status.ACTIVO, type);

        if (achievements.isEmpty()) {
            log.warn("⚠️ No hay logros activos del tipo {}", type);
            return;
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 3. Separar logros en dos categorías
        List<Achievement> specialAchievements = achievements.stream()
                .filter(a -> !a.getRepeatable() && a.getRequiredCount() != null)
                .collect(Collectors.toList());

        List<Achievement> standardAchievements = achievements.stream()
                .filter(Achievement::getRepeatable)
                .collect(Collectors.toList());

        log.info("🎯 Logros especiales encontrados: {}", specialAchievements.size());
        log.info("🎖️ Logros estándar encontrados: {}", standardAchievements.size());

        // 4. LÓGICA PRINCIPAL:

        // A) Verificar si debe otorgar logro ESPECIAL (1°, 3°, 5°)
        boolean grantedSpecialAchievement = false;
        for (Achievement achievement : specialAchievements) {
            if (achievement.getRequiredCount().equals((int) countCompleted)) {
                log.info("✨ Otorgando logro ESPECIAL: {} (requiere: {})",
                        achievement.getName(), achievement.getRequiredCount());
                grantUniqueAchievementIfNew(user, achievement);
                grantedSpecialAchievement = true;
                break; // Solo un logro especial por vez
            }
        }

        // B) Si NO es adopción especial (2°, 4°, 6°, etc), otorgar logro ESTÁNDAR
        if (!grantedSpecialAchievement) {
            // Solo otorgar logro estándar si NO es la primera vez
            if (countCompleted > 1) {
                for (Achievement achievement : standardAchievements) {
                    log.info("🎖️ Otorgando/Actualizando logro ESTÁNDAR: {}", achievement.getName());
                    grantOrUpdateRepeatableAchievement(user, achievement, petName);
                    break; // Solo un logro estándar (debería haber solo 1 por tipo)
                }
            } else {
                log.info("ℹ️ Primera adopción, no se otorga logro estándar");
            }
        }
    }

    /**
     * Otorga un logro único (no repetible) SÓLO si es la primera vez
     */
    private void grantUniqueAchievementIfNew(User user, Achievement achievement) {
        if (userAchievementRepository.existsByUserIdAndAchievementId(user.getId(), achievement.getId())) {
            log.info("ℹ️ Logro Único ya obtenido: {}", achievement.getName());
            return;
        }

        // Primera vez que obtiene el logro
        UserAchievement newUserAchievement = new UserAchievement(user, achievement);
        userAchievementRepository.save(newUserAchievement);

        // Otorgar XP
        int xpEarned = achievement.getPoints() != null ? achievement.getPoints() : 0;
        user.setUserXp(user.getUserXp() + xpEarned);
        userRepository.save(user);

        log.info("✅ Logro ESPECIAL Otorgado: {} a {} (+{} XP)",
                achievement.getName(), user.getName(), xpEarned);

        // Notificar al usuario
        /*
         * notificationService.createNotificationForUser(
         * user.getId(),
         * "🏆 ¡Nuevo logro desbloqueado!",
         * String.format("¡Felicidades! Has obtenido el logro '%s'. %s (+%d XP)",
         * achievement.getName(),
         * achievement.getPhrase(),
         * xpEarned),
         * NotificationType.LOGRO,
         * NotificationChannel.BOTH,
         * "/logros");
         */
    }

    /**
     * Otorga un logro repetible (estándar)
     */
    private void grantOrUpdateRepeatableAchievement(User user, Achievement achievement, String context) {
        Optional<UserAchievement> existing = userAchievementRepository
                .findByUserIdAndAchievementId(user.getId(), achievement.getId());

        if (existing.isPresent()) {
            // Ya tiene el logro, incrementar contador
            UserAchievement userAchievement = existing.get();
            userAchievement.setTimesCompleted(userAchievement.getTimesCompleted() + 1);
            userAchievement.setCompletedAt(LocalDateTime.now());
            userAchievementRepository.save(userAchievement);

            log.info("🔄 Logro ESTÁNDAR incrementado: {} para usuario {} ({}x)",
                    achievement.getName(), user.getName(), userAchievement.getTimesCompleted());

            // Notificación más sutil
            /*
             * notificationService.createNotificationForUser(
             * user.getId(),
             * "🎖️ ¡Progreso en logro!",
             * String.format("Has completado '%s' por %da vez. ¡Sigue así!",
             * achievement.getName(), userAchievement.getTimesCompleted()),
             * NotificationType.LOGRO,
             * NotificationChannel.BOTH,
             * "/logros");
             */

        } else {
            // Primera vez que obtiene el logro estándar
            UserAchievement newUserAchievement = new UserAchievement(user, achievement);
            userAchievementRepository.save(newUserAchievement);

            // Otorgar XP de primera vez
            int xpEarned = achievement.getPoints() != null ? achievement.getPoints() : 0;
            user.setUserXp(user.getUserXp() + xpEarned);
            userRepository.save(user);

            log.info("✅ Logro ESTÁNDAR Otorgado por primera vez: {} a {} (+{} XP)",
                    achievement.getName(), user.getName(), xpEarned);

            // Notificación
            /*
             * notificationService.createNotificationForUser(
             * user.getId(),
             * "🎖️ ¡Nuevo badge desbloqueado!",
             * String.format("Has obtenido el badge '%s'. %s (+%d XP)",
             * achievement.getName(),
             * achievement.getPhrase(),
             * xpEarned),
             * NotificationType.LOGRO,
             * NotificationChannel.BOTH,
             * "/logros");
             */
        }
    }

    /**
     * Cuenta las acciones completadas según el tipo de logro
     */
    private long getCompletedCount(UUID userId, AchievementType type) {
        if (type == AchievementType.USUARIO_ADOPTANTE) {
            // Contar solicitudes ACEPTADAS como adoptante
            return adoptionRequestRepository.countByApplicantIdAndStatus(userId, Status.ACEPTADO);
        } else if (type == AchievementType.USUARIO_RESCATISTA) {
            // Contar publicaciones ADOPTADAS como rescatista
            return publicationRepository.countByUserIdAndStatus(userId, Status.ADOPTADO);
        }
        return 0;
    }

    /**
     * Obtener todos los logros de un usuario (devuelve DTOs)
     */
    public List<UserAchievementResponseDTO> getUserAchievementsDTO(UUID userId) {
        return userAchievementRepository.findByUserId(userId).stream()
                .map(UserAchievementMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener todos los logros de un usuario (devuelve entidades)
     */
    public List<UserAchievement> getUserAchievements(UUID userId) {
        return userAchievementRepository.findByUserId(userId);
    }

    /**
     * Contar logros completados
     */
    public long countUserAchievements(UUID userId) {
        return userAchievementRepository.countByUserId(userId);
    }

    /**
     * Verificar si un usuario tiene un logro específico
     */
    public boolean hasAchievement(UUID userId, UUID achievementId) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }
}