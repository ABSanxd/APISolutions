package com.api.modules.user.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.api.common.enums.Status;
import com.api.common.enums.UserLevel;

import lombok.Data;

@Data
public class UserResponseDTO {
    private UUID id;
    private String name;
    private String email;
    private int maxPets;
    private UserLevel userLevel;
    private int userXp;
    private String department;
    private String province;
    private String district;
    private LocalDate birthDate;
    private Status status;
    private LocalDateTime createdAt;
}