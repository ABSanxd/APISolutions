package com.api.modules.advertisement.dto;

import java.time.LocalDate;

import com.api.common.enums.PriorityAd;
import com.api.common.enums.Status;
import com.api.common.enums.ZoneAd;
import com.api.modules.advertisement.model.ContactDetails;

import lombok.Data;

@Data
public class AdvertisementUpdateDTO {
    private String name;
    private String description;
    private String link;
    private String imageUrl;
    private ZoneAd zone;
    private PriorityAd priority;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isRenewable;
    private ContactDetails contact;
    private Status status;
}
