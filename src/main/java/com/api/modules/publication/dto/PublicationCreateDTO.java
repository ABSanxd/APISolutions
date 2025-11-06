package com.api.modules.publication.dto;

import java.util.List; // <--- AÑADIDO
import java.util.Map;

import com.api.common.enums.Species;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size; // <--- AÑADIDO
import lombok.Data;

@Data
public class PublicationCreateDTO {

    @NotBlank
	private String tempName;

	@NotNull
	private Species species;

	@NotBlank
	private String approxAge;

    // --- CAMBIO AQUÍ ---
	@NotNull
    @Size(min = 1, message = "Debe subir al menos una foto")
	private List<String> photos;
    // --- FIN DEL CAMBIO ---

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