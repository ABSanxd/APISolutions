package com.api.modules.publication.dto;

import java.util.Map;

import com.api.common.enums.Status;
import com.api.common.enums.species;

import lombok.Data;

@Data
public class PublicationUpdateDTO {
	private String tempName;
	private species species;
	private String approxAge;
	private String photo;
	private String description;
	private Map<String, Object> contact;
	private Status status;
	private String department;
	private String province;
	private String district;
}