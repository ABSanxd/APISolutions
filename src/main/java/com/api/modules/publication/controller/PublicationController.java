package com.api.modules.publication.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.common.response.ApiResponse;
import com.api.modules.publication.dto.PublicationCreateDTO;
import com.api.modules.publication.dto.PublicationResponseDTO;
import com.api.modules.publication.dto.PublicationUpdateDTO;
import com.api.modules.publication.service.PublicationService;
import com.api.modules.user.model.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/publications")
@RequiredArgsConstructor
public class PublicationController {

	private final PublicationService service;

	@PostMapping
	public ResponseEntity<ApiResponse<PublicationResponseDTO>> create(
			@AuthenticationPrincipal User user, 
			@Validated @RequestBody PublicationCreateDTO dto) {
		// ... (sin cambios) ...
		if (user == null) {
			return ResponseEntity.status(401)
					.body(ApiResponse.fail("Usuario no autenticado", 401));
		}
		String userId = user.getId().toString();
		System.out.println("=== CREATE PUBLICATION ===");
		System.out.println("userId from principal: " + userId);
		System.out.println("tempName: " + dto.getTempName());
		System.out.println("species: " + dto.getSpecies());
		System.out.println("photos count: " + (dto.getPhotos() != null ? dto.getPhotos().size() : 0));
		System.out.println("========================");
		return ResponseEntity.ok(service.create(userId, dto));
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<PublicationResponseDTO>>> list(
			@AuthenticationPrincipal User user, 
			@RequestParam(value = "view", defaultValue = "all") String view) {
		// ... (sin cambios) ...
		if (user == null) {
			return ResponseEntity.status(401)
					.body(ApiResponse.fail("Usuario no autenticado", 401));
		}
		String userId = user.getId().toString();
		System.out.println("GET /publications?view=" + view + " - userId: " + userId);
		switch (view) {
			case "available":
				return ResponseEntity.ok(service.listAvailable(userId));
			case "mine":
				return ResponseEntity.ok(service.listByUserId(userId));
			case "all":
			default:
				return ResponseEntity.ok(service.listAll());
		}
	}

	// --- INICIO DE CAMBIOS ---
	// AHORA REQUIERE AUTENTICACIÓN
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<PublicationResponseDTO>> get(
			@PathVariable UUID id,
			@AuthenticationPrincipal User user // <-- AÑADIDO
	) {
		if (user == null) {
			return ResponseEntity.status(401).body(ApiResponse.fail("Usuario no autenticado", 401));
		}
		return ResponseEntity.ok(service.getById(id, user.getId())); // <-- AÑADIDO user.getId()
	}
	// --- FIN DE CAMBIOS ---

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<PublicationResponseDTO>> update(
			@PathVariable UUID id,
			@RequestBody PublicationUpdateDTO dto) {
		// ... (sin cambios) ...
		return ResponseEntity.ok(service.update(id, dto));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Object>> delete(@PathVariable UUID id) {
		// ... (sin cambios) ...
		return ResponseEntity.ok(service.delete(id));
	}

	// --- INICIO DE CAMBIOS ---
	// AHORA REQUIERE AUTENTICACIÓN
	@PatchMapping("/{id}/like")
	public ResponseEntity<ApiResponse<PublicationResponseDTO>> like(
			@PathVariable UUID id,
			@AuthenticationPrincipal User user // <-- AÑADIDO
	) {
		if (user == null) {
			return ResponseEntity.status(401).body(ApiResponse.fail("Usuario no autenticado", 401));
		}
		return ResponseEntity.ok(service.toggleLikePublication(id, user)); // <-- Llama al nuevo método
	}
	// --- FIN DE CAMBIOS ---

	@PatchMapping("/{id}/share")
	public ResponseEntity<ApiResponse<PublicationResponseDTO>> share(@PathVariable UUID id) {
		// ... (sin cambios) ...
		return ResponseEntity.ok(service.sharePublication(id));
	}
}