package com.api.modules.notification.mapper;

import com.api.common.enums.Status;
import com.api.modules.notification.dto.NotificationCreateDTO;
import com.api.modules.notification.dto.NotificationResponseDTO;
import com.api.modules.notification.model.Notification;
import com.api.modules.user.model.User;

public class NotificationMapper {

    public static Notification toEntity(NotificationCreateDTO dto, User user) {
        Notification notification = new Notification();
        notification.setTitle(dto.getTitle());
        notification.setMessage(dto.getMessage());
        notification.setType(dto.getType());
        notification.setChannel(dto.getChannel());
        notification.setStatus(Status.PENDIENTE);
        notification.setActionUrl(dto.getActionUrl());
        notification.setUser(user);
        return notification;
    }

    public static NotificationResponseDTO toResponseDTO(Notification notification) {
        NotificationResponseDTO dto = new NotificationResponseDTO();
        dto.setId(notification.getId());
        dto.setTitle(notification.getTitle());
        dto.setMessage(notification.getMessage());
        dto.setType(notification.getType());
        dto.setChannel(notification.getChannel());
        dto.setStatus(notification.getStatus());
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setActionUrl(notification.getActionUrl()); 
        return dto;
    }
}
