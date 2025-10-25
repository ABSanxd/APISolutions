package com.api.modules.publication.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.common.response.ApiResponse;
import com.api.modules.publication.dto.ControllerCreateDTO;
import com.api.modules.publication.dto.PublicartionResponseDTO;
import com.api.modules.publication.dto.PublicationUpdateDTO;
import com.api.modules.publication.service.PublicationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/publications")
@RequiredArgsConstructor
public class PublicationController {

	private final PublicationService service;

	@PostMapping
	public ResponseEntity<ApiResponse<PublicartionResponseDTO>> create(
			@RequestHeader(value = "X-User-Id", required = false) String userId,
			@Validated @RequestBody ControllerCreateDTO dto) {

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

	// Obtener todas las publicaciones O solo las del usuario si se proporciona X-User-Id
	@GetMapping
	public ResponseEntity<ApiResponse<List<PublicartionResponseDTO>>> list(
			@RequestHeader(value = "X-User-Id", required = false) String userId,
			@RequestParam(value = "myPublications", required = false, defaultValue = "false") boolean myPublications) {
		
		// Si se solicita solo las publicaciones del usuario
		if (myPublications && userId != null && !userId.isBlank()) {
			return ResponseEntity.ok(service.listByUserId(userId));
		}
		
		// Si se proporciona X-User-Id pero no se solicita explícitamente solo las del usuario
		// Retornar solo las del usuario (por defecto en la sección "Mis Publicaciones")
		if (userId != null && !userId.isBlank()) {
			return ResponseEntity.ok(service.listByUserId(userId));
		}
		
		// Si no hay userId, retornar todas (para vista pública de adopciones)
		return ResponseEntity.ok(service.listAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<PublicartionResponseDTO>> get(@PathVariable Long id) {
		return ResponseEntity.ok(service.getById(id));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<PublicartionResponseDTO>> update(@PathVariable Long id,
			@RequestBody PublicationUpdateDTO dto) {
		return ResponseEntity.ok(service.update(id, dto));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Object>> delete(@PathVariable Long id) {
		return ResponseEntity.ok(service.delete(id));
	}
}