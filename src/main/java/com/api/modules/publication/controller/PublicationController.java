package com.api.modules.publication.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.common.response.ApiResponse;
import com.api.modules.publication.dto.PublicationCreateDTO;
import com.api.modules.publication.dto.PublicationResponseDTO;
import com.api.modules.publication.dto.PublicationUpdateDTO;
import com.api.modules.publication.service.PublicationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/publications")
@RequiredArgsConstructor
public class PublicationController {

	private final PublicationService service;

	@PostMapping
	public ResponseEntity<ApiResponse<PublicationResponseDTO>> create(
			Authentication authentication,
			@Validated @RequestBody PublicationCreateDTO dto) {

		if (authentication == null || authentication.getName() == null) {
			return ResponseEntity.status(401)
					.body(ApiResponse.fail("Usuario no autenticado", 401));
		}

		// Obtener userId del token JWT
		String userId = authentication.getName();

		System.out.println("=== CREATE PUBLICATION ===");
		System.out.println("userId del token: " + userId);
		System.out.println("tempName: " + dto.getTempName());
		System.out.println("species: " + dto.getSpecies());
		System.out.println("photo length: " + (dto.getPhoto() != null ? dto.getPhoto().length() : 0));
		System.out.println("========================");

		return ResponseEntity.ok(service.create(userId, dto));
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<PublicationResponseDTO>>> list(
			Authentication authentication) {

		// Si hay autenticación, devolver solo las del usuario
		if (authentication != null && authentication.getName() != null) {
			String userId = authentication.getName();
			System.out.println("GET /publications - userId: " + userId);
			return ResponseEntity.ok(service.listByUserId(userId));
		}

		// Si no hay autenticación, devolver todas (para vista pública)
		return ResponseEntity.ok(service.listAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<PublicationResponseDTO>> get(@PathVariable UUID id) {
		return ResponseEntity.ok(service.getById(id));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<PublicationResponseDTO>> update(
			@PathVariable UUID id,
			@RequestBody PublicationUpdateDTO dto) {
		return ResponseEntity.ok(service.update(id, dto));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Object>> delete(@PathVariable UUID id) {
		return ResponseEntity.ok(service.delete(id));
	}
}