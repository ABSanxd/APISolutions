package com.api.modules.notification.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

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

    // Crear y enviar notificación
    public NotificationResponseDTO createNotification(NotificationCreateDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado para la notificación."));

        Notification notification = NotificationMapper.toEntity(dto, user);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setStatus(Status.PENDIENTE);

        Notification saved = notificationRepository.save(notification);

        // Enviar por correo si el canal lo requiere
        switch (saved.getChannel()) {
            case EMAIL -> emailService.sendNotificationEmail(saved);
            case PUSH -> {} 
            case INTERNAL -> {}
            case BOTH -> emailService.sendNotificationEmail(saved);
        }

        saved.setStatus(Status.ENVIADO);
        saved.setSentAt(LocalDateTime.now());
        notificationRepository.save(saved);

        return NotificationMapper.toResponseDTO(saved);
    }

    // Listar todas las notificaciones
    public List<NotificationResponseDTO> getAllNotifications() {
        return notificationRepository.findAll()
                .stream()
                .map(NotificationMapper::toResponseDTO)
                .toList();
    }

    // Obtener por ID
    public NotificationResponseDTO getNotificationById(UUID id) {
        return notificationRepository.findById(id)
                .map(NotificationMapper::toResponseDTO)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada."));
    }

    // Eliminar una notificación
    public void deleteNotification(UUID id) {
        if (!notificationRepository.existsById(id)) {
            throw new RuntimeException("Notificación no encontrada para eliminar.");
        }
        notificationRepository.deleteById(id);
    }
}
