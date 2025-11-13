package com.api.modules.achievement.dto;

import com.api.common.enums.AchievementType;
import com.api.common.enums.Status;

import lombok.Data;

@Data
public class AchievementUpdateDTO {
    
     private String name;
    private String description;
    private String phrase;
    private AchievementType achievementType;
    private Integer points;
    private Boolean repeatable;
    private Status status;
}
