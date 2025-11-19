package com.api.modules.userAchievement.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.api.modules.userAchievement.dto.UserAchievementResponseDTO;
import com.api.modules.userAchievement.model.UserAchievement;

public class UserAchievementMapper {

    public static UserAchievementResponseDTO toResponseDTO(UserAchievement userAchievement) {
        return new UserAchievementResponseDTO(
                userAchievement.getId(),
                userAchievement.getCompletedAt(),
                userAchievement.getTimesCompleted(),

                userAchievement.getUser().getId(),
                userAchievement.getUser().getName(),

                userAchievement.getAchievement().getId(),
                userAchievement.getAchievement().getName(),
                userAchievement.getAchievement().getDescription(),
                userAchievement.getAchievement().getPhrase(),
                userAchievement.getAchievement().getAchievementType(),
                userAchievement.getAchievement().getPoints(),
                userAchievement.getAchievement().getRepeatable(),
                userAchievement.getAchievement().getRequiredCount());
    }

    public static List<UserAchievementResponseDTO> toResponseDTOList(List<UserAchievement> userAchievements) {
        return userAchievements.stream()
                .map(UserAchievementMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}