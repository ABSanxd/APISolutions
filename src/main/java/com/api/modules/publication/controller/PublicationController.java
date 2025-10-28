package com.api.modules.publication.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.common.response.ApiResponse;
import com.api.modules.publication.dto.PublicationCreateDTO;
import com.api.modules.publication.dto.PublicationResponseDTO;
import com.api.modules.publication.dto.PublicationUpdateDTO;
import com.api.modules.publication.service.PublicationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/publications")
public class PublicationController {
	private final PublicationService service;

	@PostMapping
	public ResponseEntity<ApiResponse<PublicationResponseDTO>> create(
			@RequestHeader(value = "X-User-Id", required = false) String userId,
			@Validated @RequestBody PublicationCreateDTO dto) {

		String resolvedUserId = userId;
		if ((resolvedUserId == null || resolvedUserId.isBlank()) && dto.getUserId() != null) {
			resolvedUserId = dto.getUserId().toString();
		}

		if (resolvedUserId == null || resolvedUserId.isBlank()) {
			return ResponseEntity.badRequest()
					.body(com.api.common.response.ApiResponse.fail("Header X-User-Id obligatorio (o incluir userId en body)", 400));
		}

		return ResponseEntity.ok(service.create(resolvedUserId, dto));
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<PublicationResponseDTO>>> list() {
		return ResponseEntity.ok(service.listAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<PublicationResponseDTO>> get(@PathVariable UUID id) {
		return ResponseEntity.ok(service.getById(id));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<PublicationResponseDTO>> update(@PathVariable UUID id,
			@RequestBody PublicationUpdateDTO dto) {
		return ResponseEntity.ok(service.update(id, dto));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Object>> delete(@PathVariable UUID id) {
		return ResponseEntity.ok(service.delete(id));
	}
}