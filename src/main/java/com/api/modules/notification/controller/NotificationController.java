package com.api.modules.notification.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.api.common.response.ApiResponse;
import com.api.modules.notification.dto.NotificationCreateDTO;
import com.api.modules.notification.dto.NotificationResponseDTO;
import com.api.modules.notification.service.NotificationService;
import com.api.modules.user.model.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<ApiResponse<NotificationResponseDTO>> createNotification(
            @Valid @RequestBody NotificationCreateDTO dto,
            @AuthenticationPrincipal User user) {

        NotificationResponseDTO created = notificationService.createNotification(dto, user.getId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Notificación creada exitosamente"));
    }

    @GetMapping("/mine")
    public ResponseEntity<ApiResponse<List<NotificationResponseDTO>>> getMyNotifications(
            @AuthenticationPrincipal User user) {

        List<NotificationResponseDTO> notifications = notificationService.getMyNotifications(user.getId());
        return ResponseEntity.ok(ApiResponse.success(notifications, "Notificaciones obtenidas correctamente"));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user) {

        notificationService.markAsRead(id, user.getId());
        return ResponseEntity.ok(ApiResponse.success(null, "Notificación marcada como leída"));
    }
}
