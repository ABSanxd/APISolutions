package com.api.modules.publication.dto;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import com.api.common.enums.Status;
import com.api.common.enums.Species;

import lombok.Data;

@Data
public class PublicationResponseDTO {
    
	private UUID id;
	private String tempName;
	private Species species;
	private String approxAge;
	private String photo;
	private String description;
	private Map<String, Object> contact; // json object
	private Status status;
	private LocalDate creationDate;
	private LocalDate updateDate;
	private UUID userId;
	private String otros;
}