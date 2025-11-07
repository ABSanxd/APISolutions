package com.api.modules.petchallenge.mapper;

import com.api.modules.petchallenge.dto.PetChallengeResponseDTO;
import com.api.modules.petchallenge.models.PetChallenge;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class PetChallengeMapper {
    public static PetChallengeResponseDTO toResponseDTO(PetChallenge petChallenge) {
        if (petChallenge == null) {
            return null;
        }

        PetChallengeResponseDTO dto = new PetChallengeResponseDTO();
        dto.setId(petChallenge.getId());
        dto.setPetId(petChallenge.getPet().getId());
        dto.setChallengeId(petChallenge.getChallenge().getId());
        dto.setCreatedAt(petChallenge.getCreatedAt());
        dto.setChallengeName(petChallenge.getChallenge().getName());
        dto.setPointsEarned(petChallenge.getChallenge().getPoints());
        return dto;
    }
    // Método para mapear una lista
    public static List<PetChallengeResponseDTO> toResponseDTOList(List<PetChallenge> petChallenges) {
        return petChallenges.stream()
                .map(PetChallengeMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

}
