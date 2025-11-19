package com.api.modules.notification.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.api.common.response.ApiResponse;
import com.api.modules.auth.security.JwtUtils;
import com.api.modules.notification.dto.NotificationCreateDTO;
import com.api.modules.notification.dto.NotificationResponseDTO;
import com.api.modules.notification.service.NotificationService;
import com.api.modules.user.model.User;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
@Tag(name = "Notificaciones", description = "API para gestion de notificacion del sistema")
@SecurityRequirement(name ="Bearer Authentication")
public class NotificationController {

    private final NotificationService notificationService;
    private final JwtUtils jwtUtils;

    @Operation(summary = "Crear nueva notificación")

    @PostMapping
    public ResponseEntity<ApiResponse<NotificationResponseDTO>> createNotification(
            @Valid @RequestBody NotificationCreateDTO dto,
            @Parameter(hidden = true)
            @AuthenticationPrincipal User user) {

        NotificationResponseDTO created = notificationService.createNotification(dto, user.getId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Notificación creada exitosamente"));
    }

    @Operation(summary = "Obtener mis notificaciones")

    @GetMapping("/mine")
    public ResponseEntity<ApiResponse<Page<NotificationResponseDTO>>> getMyNotifications(
            @Parameter(hidden = true) @AuthenticationPrincipal User user,
            @Parameter(description = "Numero de pagina") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Elementos por pagina") @RequestParam(defaultValue = "10") int size) {

        Page<NotificationResponseDTO> notifications = notificationService.getMyNotifications(user.getId(), page, size);
        return ResponseEntity.ok(ApiResponse.success(notifications, "Notificaciones obtenidas correctamente"));
    }

    @Operation(summary = "Marcar notificación como leída")
    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(
            @Parameter(description = "ID de la notificación")  @PathVariable UUID id,
            @Parameter(hidden = true) @AuthenticationPrincipal User user) {

        notificationService.markAsRead(id, user.getId());
        return ResponseEntity.ok(ApiResponse.success(null, "Notificación marcada como leída"));
    }

    @Operation(
        summary = "Conectarse al stream de notificaciones en tiempo real"
    )

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream( @Parameter(description = "Token JWT") @RequestParam("token") String token) {

        if (token == null || token.isEmpty() || !jwtUtils.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido");
        }

        String userId = jwtUtils.getUserIdFromToken(token);
        return notificationService.subscribe(UUID.fromString(userId));
    }
}
