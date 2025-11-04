package com.api.modules.challenge.dto;
import com.api.common.enums.Category;
import com.api.common.enums.Frequency;

import lombok.Data;

import java.util.UUID;

@Data
public class ChallengeDTO {
    private UUID id;
    private String name;
    private String description;
    private Frequency frequency;
    private Integer points;
    private Category category;
    private String image;
}
