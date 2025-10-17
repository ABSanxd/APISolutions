package com.api.modules.user.mapper;

import com.api.modules.user.dto.UserCreateDTO;
import com.api.modules.user.dto.UserResponseDTO;
import com.api.modules.user.dto.UserUpdateDTO;
import com.api.modules.user.model.User;

public class UserMapper {

    public static User toEntity(UserCreateDTO dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setDepartment(dto.getDepartment());
        user.setProvince(dto.getProvince());
        user.setDistrict(dto.getDistrict());
        user.setBirthDate(dto.getBirthDate());
        return user;
    }

    public static void updateEntity(User user, UserUpdateDTO dto) {
        if (dto.getName() != null)
            user.setName(dto.getName());
        if (dto.getPassword() != null)
            user.setPassword(dto.getPassword()); 
        if (dto.getDepartment() != null)
            user.setDepartment(dto.getDepartment());
        if (dto.getProvince() != null)
            user.setProvince(dto.getProvince());
        if (dto.getDistrict() != null)
            user.setDistrict(dto.getDistrict());
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
        dto.setDepartment(user.getDepartment());
        dto.setProvince(user.getProvince());
        dto.setDistrict(user.getDistrict());
        dto.setBirthDate(user.getBirthDate());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
}