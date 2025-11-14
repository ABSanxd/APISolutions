package com.api.modules.petAchievement.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.api.modules.petAchievement.dto.PetAchievementResponseDTO;
import com.api.modules.petAchievement.model.PetAchievement;

public class PetAchievementMapper {

    public static PetAchievementResponseDTO toResponseDTO(PetAchievement petAchievement) {
        return new PetAchievementResponseDTO(
            petAchievement.getId(),
            petAchievement.getPet().getId(),
            petAchievement.getPet().getNombre(),
            petAchievement.getAchievement().getId(),
            petAchievement.getAchievement().getName(),
            petAchievement.getAchievement().getDescription(),
            petAchievement.getAchievement().getPhrase(),
            petAchievement.getAchievement().getAchievementType(),
            //petAchievement.getAchievement().getPoints(), no por ahora
            petAchievement.getAchievement().getRepeatable(),
            petAchievement.getAchievement().getCountFromCreation(),
            petAchievement.getPeriodStart(),
            petAchievement.getPeriodEnd(),
            petAchievement.getStatus(),
            petAchievement.getCompletedAt(),
            petAchievement.getCreatedAt()
        );
    }

    public static List<PetAchievementResponseDTO> toResponseDTOList(List<PetAchievement> petAchievements) {
        return petAchievements.stream()
                .map(PetAchievementMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}