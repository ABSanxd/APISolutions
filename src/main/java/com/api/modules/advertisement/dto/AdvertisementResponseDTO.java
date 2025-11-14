package com.api.modules.advertisement.dto;

import java.time.LocalDate;
import java.util.UUID;

import com.api.common.enums.PriorityAd;
import com.api.common.enums.Status;
import com.api.common.enums.ZoneAd;
import com.api.modules.advertisement.model.ContactDetails;

import lombok.Data;

@Data
public class AdvertisementResponseDTO {
    private UUID id;
    private String name;
    private String description;
    private String link;
    private String imageUrl;
    private ZoneAd zone;
    private PriorityAd priority;
    private ContactDetails contact;
    private LocalDate startDate;
    private LocalDate endDate;
    private Status status;
}
