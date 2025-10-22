package com.api.modules.publication.dto;

import java.util.Map;
import java.util.UUID;

import com.api.common.enums.species;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ControllerCreateDTO {
	@NotBlank
	private String tempName;

	@NotNull
	private species species;

	@NotBlank
	private String approxAge;

	@NotBlank
	private String photo;

	@NotBlank
	private String description;

	// contact as raw JSON string
	@NotNull
	private Map<String, Object> contact;

	// Optional extra JSON when species == OTRO
	private String otros;

	// optional fallback: allow client to send userId in body (no validation) to avoid binding errors
	private UUID userId;
}
