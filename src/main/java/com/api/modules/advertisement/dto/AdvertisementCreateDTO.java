package com.api.modules.advertisement.dto;

import java.time.LocalDate;

import com.api.common.enums.PriorityAd;
import com.api.common.enums.Status;
import com.api.common.enums.ZoneAd;
import com.api.modules.advertisement.model.ContactDetails;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdvertisementCreateDTO {
    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    private String description;

    private String link;

    private String imageUrl;

    @NotNull(message = "La zona es obligatoria")
    private ZoneAd zone;

    @NotNull(message = "La prioridad es obligatoria")
    private PriorityAd priority;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate startDate;

    @NotNull(message = "La fecha de fin es obligatoria")
    private LocalDate endDate;

    private Boolean isRenewable = false;

    private ContactDetails contact;

    private Status status = Status.ACTIVO;
}
