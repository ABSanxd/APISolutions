package com.api.modules.achievement.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.api.common.enums.AchievementType;
import com.api.common.enums.Status;

import lombok.Data;

@Data

public class AchievementResponseDTO {
    private UUID id;
    private String name;
    private String description;
    private String phrase;
    private AchievementType achievementType;
    private Integer points;
    private Boolean repeatable;
    private Status status;
    private Boolean countFromCreation;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
