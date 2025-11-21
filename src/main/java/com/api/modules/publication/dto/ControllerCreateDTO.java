package com.api.modules.publication.dto;

import java.util.Map;

import com.api.common.enums.Species;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ControllerCreateDTO {
	@NotBlank
	private String tempName;

	@NotNull
	private Species species;

	@NotBlank
	private String approxAge;

	@NotBlank
	private String photo;

	@NotBlank
	private String description;

	@NotNull
	private Map<String, Object> contact;

	@NotBlank
	private String department;

	@NotBlank
	private String province;

	@NotBlank
	private String district;
}