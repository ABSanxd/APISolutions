package com.api.modules.challenge.mapper;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import com.api.modules.challenge.model.Challenge;
import com.api.modules.challenge.dto.ChallengeCreateDTO;
import com.api.modules.challenge.dto.ChallengeResponseDTO;
import com.api.modules.challenge.dto.ChallengeUpdateDTO;

@Component
public class ChallengeMapper {
    public ChallengeResponseDTO toResponseDTO(Challenge entity){
        if(entity == null){
            return null;
        }

        ChallengeResponseDTO dto = new ChallengeResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setFrequency(entity.getFrequency());
        dto.setPoints(entity.getPoints());
        dto.setCategory(entity.getCategory());
        dto.setImage(entity.getImage());
        dto.setStatus(entity.getStatus());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        
        return dto;


    }


    public Challenge toEntity(ChallengeCreateDTO dto) {
        if (dto == null) {
            return null;
        }
        Challenge entity = new Challenge();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setFrequency(dto.getFrequency());
        entity.setPoints(dto.getPoints());
        entity.setCategory(dto.getCategory());
        entity.setImage(dto.getImage());
        return entity;
    }

    public static void updateEntity(Challenge challenge, ChallengeUpdateDTO dto) {
        if (dto.getName() != null) {
            challenge.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            challenge.setDescription(dto.getDescription());
        }
        if (dto.getFrequency() != null) {
            challenge.setFrequency(dto.getFrequency());
        }
        if (dto.getPoints() != null) {
            challenge.setPoints(dto.getPoints());
        }
        if (dto.getCategory() != null) {
            challenge.setCategory(dto.getCategory());
        }
        if (dto.getImage() != null) {
            challenge.setImage(dto.getImage());
        }
        if (dto.getStatus() != null) {
            challenge.setStatus(dto.getStatus());
        }
        
        challenge.setUpdatedAt(LocalDateTime.now());
    }


    
   
}

