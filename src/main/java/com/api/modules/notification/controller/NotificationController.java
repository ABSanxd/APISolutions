package com.api.modules.notification.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.api.common.response.ApiResponse;
import com.api.modules.notification.dto.NotificationCreateDTO;
import com.api.modules.notification.dto.NotificationResponseDTO;
import com.api.modules.notification.service.NotificationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationResponseDTO>>> getAllNotifications() {
        List<NotificationResponseDTO> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(ApiResponse.success(notifications, "Notificaciones obtenidas correctamente"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NotificationResponseDTO>> getNotificationById(@PathVariable UUID id) {
        NotificationResponseDTO notification = notificationService.getNotificationById(id);
        return ResponseEntity.ok(ApiResponse.success(notification, "Notificación encontrada"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<NotificationResponseDTO>> createNotification(
            @Valid @RequestBody NotificationCreateDTO dto) {
        NotificationResponseDTO created = notificationService.createNotification(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Notificación creada exitosamente"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteNotification(@PathVariable UUID id) {
        notificationService.deleteNotification(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(ApiResponse.success(null, "Notificación eliminada correctamente"));
    }
}
