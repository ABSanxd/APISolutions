package com.api.modules.publication.dto;

import java.util.List; // <--- AÑADIDO
import java.util.Map;

import com.api.common.enums.Especies;
import com.api.common.enums.Status;

import lombok.Data;

@Data
public class PublicationUpdateDTO {
	private String tempName;
	private Especies species;
	private String approxAge;
	private List<String> photos;
	private String description;
	private Map<String, Object> contact;
	private Status status;
	private String department;
	private String province;
	private String district;
}