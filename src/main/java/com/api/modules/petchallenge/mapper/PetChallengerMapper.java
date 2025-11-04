package com.api.modules.petchallenge.mapper;
import com.api.modules.petchallenge.dto.PetChallengeResponseDTO;
import com.api.modules.petchallenge.models.PetChallenge;
import org.springframework.stereotype.Component;
@Component
public class PetChallengerMapper {
    public PetChallengeResponseDTO toDTO(PetChallenge entity){
        if(entity == null){
            return null;
        }

        PetChallengeResponseDTO dto = new PetChallengeResponseDTO();
        dto.setId(entity.getId());
        dto.setPetId(entity.getPet().getId());
        dto.setChallengeId(entity.getChallenge().getId());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }
    
    
}
