package com.api.modules.challenge.mapper;
import org.springframework.stereotype.Component;
import com.api.modules.challenge.model.Challenge;
import com.api.modules.challenge.dto.ChallengeCreateDTO;
import com.api.modules.challenge.dto.ChallengeResponseDTO;
import com.api.modules.challenge.dto.ChallengeUpdateDTO;

@Component
public class ChallengeMapper {
    public ChallengeResponseDTO toDTO(Challenge entity){
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
                if (entity.getStatus() != null) {
            dto.setStatus(entity.getStatus().toString()); // Convert enum to string
        }
        
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

    public Challenge toEntity(ChallengeUpdateDTO dto) {
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
        
        // Assuming the Status enum in ChallengeUpdateDTO
        if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus()); // Assuming Challenge entity has a 'status' field of Status enum type
        }
        
        return entity;
    }

    
   
}

