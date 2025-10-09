package com.api.modules.user.mapper;

import com.api.modules.user.dto.UserRequestDTO;
import com.api.modules.user.dto.UserResponseDTO;
import com.api.modules.user.model.User;

public class UserMapper {

    public static User toEntity(UserRequestDTO dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        return user;
    }

    public static UserResponseDTO toResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setMaxPets(user.getMaxPets());
        dto.setUserLevel(user.getUserLevel());
        dto.setUserXp(user.getUserXp());
        dto.setStatus(user.getStatus());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
}