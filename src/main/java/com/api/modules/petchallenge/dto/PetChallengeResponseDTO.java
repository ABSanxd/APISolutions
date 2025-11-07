package com.api.modules.petchallenge.dto;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;

@Data
public class PetChallengeResponseDTO {
    private UUID id;
    private UUID petId;
    private UUID challengeId;
    private LocalDateTime createdAt;

    //detalle de reto completado
    private String challengeName;
    private Integer pointsEarned;
}
