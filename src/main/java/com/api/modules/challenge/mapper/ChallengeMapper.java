package com.api.modules.challenge.mapper;
import org.springframework.stereotype.Component;
import com.api.modules.challenge.model.Challenge;
import com.api.modules.challenge.dto.ChallengeCreateDTO;
import com.api.modules.challenge.dto.ChallengeDTO;

@Component
public class ChallengeMapper {
    public ChallengeDTO toDTO(Challenge entity){
        if(entity == null){
            return null;
        }

        ChallengeDTO dto = new ChallengeDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setFrequency(entity.getFrequency());
        dto.setPoints(entity.getPoints());
        dto.setCategory(entity.getCategory());
        dto.setImage(entity.getImage());
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

    
   
}

