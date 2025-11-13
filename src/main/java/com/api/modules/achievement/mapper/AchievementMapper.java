package com.api.modules.achievement.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.api.modules.achievement.dto.AchievementCreateDTO;
import com.api.modules.achievement.dto.AchievementResponseDTO;
import com.api.modules.achievement.dto.AchievementUpdateDTO;
import com.api.modules.achievement.model.Achievement;

public class AchievementMapper {

    public static AchievementResponseDTO toResponseDTO(Achievement achievement) {
        AchievementResponseDTO dto = new AchievementResponseDTO();
        dto.setId(achievement.getId());
        dto.setName(achievement.getName());
        dto.setDescription(achievement.getDescription());
        dto.setPhrase(achievement.getPhrase());
        dto.setAchievementType(achievement.getAchievementType());
        dto.setPoints(achievement.getPoints());
        dto.setRepeatable(achievement.getRepeatable());
        dto.setStatus(achievement.getStatus());
        dto.setCreatedAt(achievement.getCreatedAt());
        dto.setUpdatedAt(achievement.getUpdatedAt());
        return dto;
    }

    public static List<AchievementResponseDTO> toResponseDTOList(List<Achievement> achievements) {
        return achievements.stream()
                .map(AchievementMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public static Achievement toEntity(AchievementCreateDTO dto) {
        Achievement achievement = new Achievement();
        achievement.setName(dto.getName());
        achievement.setDescription(dto.getDescription());
        achievement.setPhrase(dto.getPhrase());
        achievement.setAchievementType(dto.getAchievementType());
        achievement.setPoints(dto.getPoints());
        achievement.setRepeatable(dto.getRepeatable());
        achievement.setStatus(dto.getStatus());

        return achievement;
    }

    public static void updateEntity(Achievement achievement, AchievementUpdateDTO dto) {
        if (dto.getName() != null)
            achievement.setName(dto.getName());
        if (dto.getDescription() != null)
            achievement.setDescription(dto.getDescription());
        if (dto.getPhrase() != null)
            achievement.setPhrase(dto.getPhrase());
        if (dto.getAchievementType() != null)
            achievement.setAchievementType(dto.getAchievementType());
        if (dto.getPoints() != null)
            achievement.setPoints(dto.getPoints());
        if (dto.getRepeatable() != null)
            achievement.setRepeatable(dto.getRepeatable());

        if (dto.getStatus() != null)
            achievement.setStatus(dto.getStatus());
        achievement.setUpdatedAt(LocalDateTime.now());
    }

}
