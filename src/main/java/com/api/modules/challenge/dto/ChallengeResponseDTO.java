package com.api.modules.challenge.dto;
import com.api.common.enums.Category;
import com.api.common.enums.Frequency;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;
import com.api.common.enums.Status;

@Data
public class ChallengeResponseDTO {
    private UUID id;
    private String name;
    private String description;
    private Frequency frequency;
    private Integer points;
    private Category category;
    private String image;   
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
