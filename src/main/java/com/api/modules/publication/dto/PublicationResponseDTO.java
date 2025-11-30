package com.api.modules.publication.dto;

import java.time.LocalDate;
import java.util.List; 
import java.util.Map;
import java.util.UUID;

import com.api.common.enums.Especies;
import com.api.common.enums.Status;

import lombok.Data;

@Data
public class PublicationResponseDTO {
	private UUID id;
	private String tempName;
	private Especies species;
	private String approxAge;

	private List<String> photos;

	private String description;
	private Map<String, Object> contact;
	private Status status;
	private LocalDate creationDate;
	private LocalDate updateDate;
	
	private UserDTO user; 
	
	private String department;
	private String province;
	private String district;
	private Integer shared;
	private Integer likes; // <-- ESTE SE QUEDA (se calculará)

	private boolean likedByMe; // <-- AÑADIR ESTE CAMPO

	@Data
	public static class UserDTO {
		private UUID id;
		private String name;
	}
}