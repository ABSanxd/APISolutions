package com.api.modules.publication.dto;

import java.util.List; 
import java.util.Map;

import com.api.common.enums.Especies;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size; 
import lombok.Data;

@Data
public class PublicationCreateDTO {

    @NotBlank
	private String tempName;

	@NotNull
	private Especies species;

	@NotBlank
	private String approxAge;

	@NotNull
    @Size(min = 1, message = "Debe subir al menos una foto")
	private List<String> photos;

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