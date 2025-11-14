package com.api.modules.achievementRequirement.mapper;

import com.api.modules.achievement.model.Achievement;
import java.util.List;
import java.util.stream.Collectors;
import com.api.modules.achievementRequirement.dto.AchievementRequirementCreateDTO;
import com.api.modules.achievementRequirement.dto.AchievementRequirementResponseDTO;
import com.api.modules.achievementRequirement.dto.AchievementRequirementUpdateDTO;
import com.api.modules.achievementRequirement.model.AchievementRequirement;
import com.api.modules.challenge.model.Challenge;

public class AchievementRequirementMapper {

    public static AchievementRequirementResponseDTO toResponseDTO(AchievementRequirement requirement) {
        return new AchievementRequirementResponseDTO(
                requirement.getId(),
                requirement.getAchievement().getId(),
                requirement.getAchievement().getName(),
                requirement.getChallenge().getId(),
                requirement.getChallenge().getName(),
                requirement.getChallenge().getCategory(),
                requirement.getRepetitions(),
                requirement.getValidationPeriod(),
                requirement.getCreatedAt());
    }

    public static List<AchievementRequirementResponseDTO> toResponseDTOList(
            List<AchievementRequirement> requirements) {
        return requirements.stream()
                .map(AchievementRequirementMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public static AchievementRequirement toEntity(
            AchievementRequirementCreateDTO dto,
            Achievement achievement,
            Challenge challenge) {
        AchievementRequirement requirement = new AchievementRequirement();
        requirement.setAchievement(achievement);
        requirement.setChallenge(challenge);
        requirement.setRepetitions(dto.repetitions());
        requirement.setValidationPeriod(dto.validationPeriod());
        return requirement;
    }

    public static void updateEntity(
            AchievementRequirement requirement,
            AchievementRequirementUpdateDTO dto,
            Challenge challenge) {
        if (dto.challengeId() != null && challenge != null)
            requirement.setChallenge(challenge);
        if (dto.repetitions() != null)
            requirement.setRepetitions(dto.repetitions());
        if (dto.validationPeriod() != null)
            requirement.setValidationPeriod(dto.validationPeriod());
    }
}