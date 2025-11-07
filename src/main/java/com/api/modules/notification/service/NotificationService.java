package com.api.modules.notification.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.common.enums.NotificationChannel;
import com.api.common.enums.NotificationType;
import com.api.common.enums.Status;
import com.api.modules.notification.dto.NotificationCreateDTO;
import com.api.modules.notification.dto.NotificationResponseDTO;
import com.api.modules.notification.mapper.NotificationMapper;
import com.api.modules.notification.model.Notification;
import com.api.modules.notification.repository.NotificationRepository;
import com.api.modules.user.model.User;
import com.api.modules.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    // Crear y enviar notificación (usado por controller o módulos)
    @Transactional
    public NotificationResponseDTO createNotification(NotificationCreateDTO dto, UUID currentUserId) {
        UUID targetUserId = dto.getUserId() != null ? dto.getUserId() : currentUserId;

        User user = userRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado para la notificación."));

        Notification notification = NotificationMapper.toEntity(dto, user);
        Notification saved = notificationRepository.save(notification);

        // Enviar por canal
        if (dto.getChannel() == NotificationChannel.EMAIL || dto.getChannel() == NotificationChannel.BOTH) {
            emailService.sendNotificationEmail(saved);
        }

        saved.setStatus(Status.ENVIADO);
        notificationRepository.save(saved);

        return NotificationMapper.toResponseDTO(saved);
    }

    // Método práctico para otros módulos
    @Transactional
    public void createNotificationForUser(UUID userId, String title, String message,
                                          NotificationType type,
                                          NotificationChannel channel,
                                          String actionUrl) {
        NotificationCreateDTO dto = new NotificationCreateDTO();
        dto.setUserId(userId);
        dto.setTitle(title);
        dto.setMessage(message);
        dto.setType(type);
        dto.setChannel(channel);
        dto.setActionUrl(actionUrl);
        createNotification(dto, userId);
    }

    // Obtener notificaciones del usuario autenticado
    public List<NotificationResponseDTO> getMyNotifications(UUID currentUserId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(currentUserId)
                .stream()
                .map(NotificationMapper::toResponseDTO)
                .toList();
    }

    // Marcar como leída
    @Transactional
    public void markAsRead(UUID notificationId, UUID userId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));

        if (!notification.getUser().getId().equals(userId)) {
            throw new RuntimeException("No tienes permiso para modificar esta notificación");
        }

        notification.setStatus(Status.LEIDO);
        notificationRepository.save(notification);
    }
}
