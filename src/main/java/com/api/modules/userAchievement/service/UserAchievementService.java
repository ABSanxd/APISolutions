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
import com.api.common.enums.NotificationChannel;
import com.api.common.enums.NotificationType;

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

    // ------------------------------------------------------------
    //     MÉTODO PRINCIPAL: OTORGAR LOGROS EN ADOPCIÓN
    // ------------------------------------------------------------

    @Transactional
    public void grantAdoptionAchievements(UUID adopterId, UUID rescuerId, String petName) {
        log.info("🏆 Otorgando logros de adopción - Adoptante: {}, Rescatista: {}", adopterId, rescuerId);

        grantAchievementByType(adopterId, AchievementType.USUARIO_ADOPTANTE, petName);
        grantAchievementByType(rescuerId, AchievementType.USUARIO_RESCATISTA, petName);
    }

    // ------------------------------------------------------------
    //     MÉTODO GENERAL PARA OTORGAR LOGROS SEGÚN EL TIPO
    // ------------------------------------------------------------

    @Transactional
    public void grantAchievementByType(UUID userId, AchievementType type, String petName) {

        long countCompleted = getCompletedCount(userId, type);

        log.info("Usuario: {} - Tipo: {} - Acciones completadas: {}", userId, type, countCompleted);

        List<Achievement> achievements = achievementRepository.findByStatusAndAchievementType(Status.ACTIVO, type);
        if (achievements.isEmpty()) {
            log.warn("No hay logros activos del tipo {}", type);
            return;
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Separar logros
        List<Achievement> specialAchievements = achievements.stream()
                .filter(a -> !a.getRepeatable() && a.getRequiredCount() != null)
                .collect(Collectors.toList());

        List<Achievement> standardAchievements = achievements.stream()
                .filter(Achievement::getRepeatable)
                .collect(Collectors.toList());

        // ------------------------------------------------------------
        //     1. Intentar dar un logro ESPECIAL (1ra, 3ra adopción)
        // ------------------------------------------------------------

        for (Achievement achievement : specialAchievements) {
            if (achievement.getRequiredCount().equals((int) countCompleted)) {
                log.info("Otorgando logro ESPECIAL: {}", achievement.getName());
                grantUniqueAchievementIfNew(user, achievement);
                return; // No se deben dar dos logros a la vez → FIN
            }
        }

        // ------------------------------------------------------------
        //     2. Si no era logro especial → dar logro estándar (2da, 4ta...)
        // ------------------------------------------------------------

        if (countCompleted > 1 && !standardAchievements.isEmpty()) {
            grantOrUpdateRepeatableAchievement(user, standardAchievements.get(0), petName);
        }
    }

    // ------------------------------------------------------------
    //     LOGRO ESPECIAL (solo una vez)
    // ------------------------------------------------------------

    private void grantUniqueAchievementIfNew(User user, Achievement achievement) {

        if (userAchievementRepository.existsByUserIdAndAchievementId(user.getId(), achievement.getId())) {
            log.info("ℹLogro ESPECIAL ya obtenido previamente: {}", achievement.getName());
            return;
        }

        UserAchievement newUserAchievement = new UserAchievement(user, achievement);
        userAchievementRepository.save(newUserAchievement);

        int xp = achievement.getPoints() != null ? achievement.getPoints() : 0;
        user.setUserXp(user.getUserXp() + xp);
        userRepository.save(user);

        notifyAchievement(user, achievement, false, 1);

        log.info("Logro ESPECIAL otorgado: {} a {} (+{} XP)",
                achievement.getName(), user.getName(), xp);
    }

    // ------------------------------------------------------------
    //     LOGRO ESTÁNDAR (repetible)
    // ------------------------------------------------------------

    private void grantOrUpdateRepeatableAchievement(User user, Achievement achievement, String context) {

        Optional<UserAchievement> existing = userAchievementRepository.findByUserIdAndAchievementId(
                user.getId(), achievement.getId());

        if (existing.isPresent()) {

            UserAchievement ua = existing.get();
            ua.setTimesCompleted(ua.getTimesCompleted() + 1);
            ua.setCompletedAt(LocalDateTime.now());
            userAchievementRepository.save(ua);

            notifyAchievement(user, achievement, true, ua.getTimesCompleted());

            log.info("Logro ESTÁNDAR incrementado: {} ({}x)",
                    achievement.getName(), ua.getTimesCompleted());
        } else {

            UserAchievement newUA = new UserAchievement(user, achievement);
            userAchievementRepository.save(newUA);

            int xp = achievement.getPoints() != null ? achievement.getPoints() : 0;
            user.setUserXp(user.getUserXp() + xp);
            userRepository.save(user);

            notifyAchievement(user, achievement, true, 1);

            log.info("Logro ESTÁNDAR otorgado por primera vez: {} (+{} XP)",
                    achievement.getName(), xp);
        }
    }

    // ------------------------------------------------------------
    //     MÉTODO UNIFICADO DE NOTIFICACIONES
    // ------------------------------------------------------------

    private void notifyAchievement(User user, Achievement achievement, boolean isRepeatable, int timesCompleted) {

        String title;
        String message;

        if (!isRepeatable) {
            title = "¡Nuevo logro desbloqueado!";
            message = String.format(
                    "¡Felicidades! Has obtenido el logro '%s'. %s",
                    achievement.getName(),
                    achievement.getPhrase()
            );
        } else if (timesCompleted == 1) {
            title = "¡Nuevo badge obtenido!";
            message = String.format(
                    "Has obtenido el badge '%s'. %s",
                    achievement.getName(),
                    achievement.getPhrase()
            );
        } else {
            title = "¡Progreso en logro!";
            message = String.format(
                    "Has completado '%s' por %dª vez. ¡Sigue así!",
                    achievement.getName(),
                    timesCompleted
            );
        }

        notificationService.createNotificationForUser(
                user.getId(),
                title,
                message,
                NotificationType.LOGRO,
                NotificationChannel.BOTH,
                "/logros"
        );
    }

    // ------------------------------------------------------------
    //     CONTADOR DE ACCIONES (según tipo de logro)
    // ------------------------------------------------------------

    private long getCompletedCount(UUID userId, AchievementType type) {

        if (type == AchievementType.USUARIO_ADOPTANTE) {
            return adoptionRequestRepository.countByApplicantIdAndStatus(userId, Status.ACEPTADO);

        } else if (type == AchievementType.USUARIO_RESCATISTA) {
            return publicationRepository.countByUserIdAndStatus(userId, Status.ADOPTADO);
        }

        return 0;
    }

    // ------------------------------------------------------------
    //     CONSULTAS PÚBLICAS
    // ------------------------------------------------------------

    public List<UserAchievementResponseDTO> getUserAchievementsDTO(UUID userId) {
        return userAchievementRepository.findByUserId(userId).stream()
                .map(UserAchievementMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<UserAchievement> getUserAchievements(UUID userId) {
        return userAchievementRepository.findByUserId(userId);
    }

    public long countUserAchievements(UUID userId) {
        return userAchievementRepository.countByUserId(userId);
    }

    public boolean hasAchievement(UUID userId, UUID achievementId) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }
}
