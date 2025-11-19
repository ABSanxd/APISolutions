package com.api.modules.userAchievement.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.common.enums.AchievementType;
import com.api.common.enums.NotificationChannel;
import com.api.common.enums.NotificationType;
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
    private final AdoptionRequestRepository adoptionRequestRepository; // para contar adopciones
    private final PublicationRepository publicationRepository; // para contar rescates

    // ... (grantAdoptionAchievements sin cambios)
    @Transactional
    public void grantAdoptionAchievements(UUID adopterId, UUID rescuerId, String petName) {
        log.info("🏆 Otorgando logros de adopción - Adoptante: {}, Rescatista: {}", adopterId, rescuerId);

        // 1. Otorgar logro al ADOPTANTE
        grantAchievementByType(adopterId, AchievementType.USUARIO_ADOPTANTE, petName);

        // 2. Otorgar logro al RESCATISTA
        grantAchievementByType(rescuerId, AchievementType.USUARIO_RESCATISTA, petName);
    }

    /**
     * Otorgar logro por tipo (ADOPTANTE o RESCATISTA) - Implementación de lógica
     * HÍBRIDA
     */
    @Transactional
    public void grantAchievementByType(UUID userId, AchievementType type, String petName) {

        // 1. Obtener el conteo actual de acciones completadas (Adopciones o Rescates)
        long countCompleted = getCompletedCount(userId, type);

        // 2. Buscar todos los logros activos de este tipo
        List<Achievement> achievements = achievementRepository
                .findByStatusAndAchievementType(Status.ACTIVO, type);

        if (achievements.isEmpty()) {
            log.warn("⚠️ No hay logros activos del tipo {}", type);
            return;
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        log.info("🏆 Analizando logros para {} - Conteo Actual: {}", user.getName(), countCompleted);

        // 3. Iterar y Otorgar/Actualizar
        for (Achievement achievement : achievements) {
            Integer requiredCount = achievement.getRequiredCount();

            // Lógica para Logros Estándar/Repetibles (Medalla Rescatista)
            if (achievement.getRepeatable()) {
                // Asumimos que el logro repetible tiene requiredCount = 1 (o null)
                // para ser otorgado en cada acción.
                if (requiredCount == null || countCompleted >= requiredCount) {
                    grantOrUpdateRepeatableAchievement(user, achievement, petName);
                }

                // Lógica para Logros Progresivos/Especiales (Corazón de Oro, Familia Creciente)
            } else {
                if (requiredCount != null && countCompleted == requiredCount) {
                    // Solo otorgar si el conteo actual COINCIDE exactamente con el requisito
                    // y solo si el logro aún no ha sido obtenido.
                    grantUniqueAchievementIfNew(user, achievement);
                }
            }
        }
    }

    /**
     * Otorga un logro único (no repetible) SÓLO si es la primera vez.
     */
    private void grantUniqueAchievementIfNew(User user, Achievement achievement) {
        if (userAchievementRepository.existsByUserIdAndAchievementId(user.getId(), achievement.getId())) {
            // Este caso no debería pasar si la lógica de 'countCompleted == requiredCount'
            // es perfecta,
            // pero es un guardrail útil.
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

        log.info("✅ Logro Progresivo Otorgado: {} a {} (+{} XP)",
                achievement.getName(), user.getName(), xpEarned);

        // Notificar al usuario (Similar a tu lógica original)
        notificationService.createNotificationForUser(
                user.getId(),
                "🏆 ¡Nuevo logro desbloqueado!",
                String.format("¡Felicidades! Has obtenido el logro '%s'. %s (+%d XP)",
                        achievement.getName(),
                        achievement.getPhrase(),
                        xpEarned),
                NotificationType.LOGRO,
                NotificationChannel.BOTH,
                "/logros");
    }

    /**
     * Otorga un logro repetible. Logro estandard: incrementa el contador cada vez
     * que se cumple la acción.
     */
    private void grantOrUpdateRepeatableAchievement(User user, Achievement achievement, String context) {
        Optional<UserAchievement> existing = userAchievementRepository
                .findByUserIdAndAchievementId(user.getId(), achievement.getId());

        if (existing.isPresent()) {
            // Actualizar contador
            UserAchievement userAchievement = existing.get();
            userAchievement.setTimesCompleted(userAchievement.getTimesCompleted() + 1);
            userAchievement.setCompletedAt(LocalDateTime.now()); // Actualiza la fecha de la última vez completada
            userAchievementRepository.save(userAchievement);

            log.info("🔄 Logro repetible incrementado: {} para usuario {} ({}x)",
                    achievement.getName(), user.getName(), userAchievement.getTimesCompleted());

            // Notificación de progreso
            // (La notificacion podría ser menos intrusiva que la de un logro Único)

        } else {
            // Primera vez que obtiene el logro repetible
            UserAchievement newUserAchievement = new UserAchievement(user, achievement);
            userAchievementRepository.save(newUserAchievement);

            // Otorgar XP de primera vez para el logro repetible
            int xpEarned = achievement.getPoints() != null ? achievement.getPoints() : 0;
            user.setUserXp(user.getUserXp() + xpEarned);
            userRepository.save(user);

            log.info("✅ Logro Repetible Otorgado por primera vez: {} a {} (+{} XP)",
                    achievement.getName(), user.getName(), xpEarned);

            // Notificar de primera vez.
        }
    }

    /**
     * Helper: Cuenta las acciones completadas según el tipo de logro.
     */
    private long getCompletedCount(UUID userId, AchievementType type) {
        if (type == AchievementType.USUARIO_ADOPTANTE) {
            // Conteo: Número de solicitudes de adopción ACEPTADAS por el usuario
            // (Adoptante)
            return adoptionRequestRepository.countByApplicantIdAndStatus(userId, Status.ACEPTADO);
        } else if (type == AchievementType.USUARIO_RESCATISTA) {
            // Conteo: Número de publicaciones que el usuario (rescatista) ha
            // completado/adoptado.
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
     * Contar logros completados
     */
    public long countUserAchievements(UUID userId) {
        return userAchievementRepository.countByUserId(userId);
    }

}