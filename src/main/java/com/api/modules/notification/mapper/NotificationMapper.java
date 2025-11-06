package com.api.modules.notification.mapper;

import com.api.modules.notification.dto.NotificationCreateDTO;
import com.api.modules.notification.dto.NotificationResponseDTO;
import com.api.modules.notification.model.Notification;
import com.api.modules.user.model.User;

public class NotificationMapper {

    public static Notification toEntity(NotificationCreateDTO dto, User user) {
        Notification n = new Notification();
        n.setUser(user);
        n.setTitle(dto.getTitle());
        n.setMessage(dto.getMessage());
        n.setType(dto.getType());
        n.setChannel(dto.getChannel());
        n.setActionUrl(dto.getActionUrl());
        n.setIcon(dto.getIcon());
        return n;
    }

    public static NotificationResponseDTO toResponseDTO(Notification n) {
        NotificationResponseDTO dto = new NotificationResponseDTO();
        dto.setId(n.getId());
        dto.setTitle(n.getTitle());
        dto.setMessage(n.getMessage());
        dto.setType(n.getType());
        dto.setChannel(n.getChannel());
        dto.setStatus(n.getStatus());
        dto.setCreatedAt(n.getCreatedAt());
        dto.setSentAt(n.getSentAt());
        dto.setActionUrl(n.getActionUrl());
        dto.setIcon(n.getIcon());
        return dto;
    }
}
