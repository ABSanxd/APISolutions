package com.api.modules.publication.dto;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import com.api.common.enums.Status;
import com.api.common.enums.species;

import lombok.Data;

@Data
public class PublicartionResponseDTO {
	private UUID id;  // Cambió de Long a UUID
	private String tempName;
	private species species;
	private String approxAge;
	private String photo;
	private String description;
	private Map<String, Object> contact;
	private Status status;
	private LocalDate creationDate;
	private LocalDate updateDate;
	private UUID userId;
	private String department;
	private String province;
	private String district;
	private Integer shared;
	private Integer likes;
}