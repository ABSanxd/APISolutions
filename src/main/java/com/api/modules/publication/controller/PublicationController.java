package com.api.modules.publication.controller;

import java.util.List;
import java.util.UUID; // Asegúrate que UUID esté importado

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
import com.api.modules.publication.dto.ControllerCreateDTO;
import com.api.modules.publication.dto.PublicartionResponseDTO;
import com.api.modules.publication.dto.PublicationUpdateDTO;
import com.api.modules.publication.service.PublicationService;
import com.api.modules.user.model.User; // Importa tu entidad User

import lombok.RequiredArgsConstructor;

@RestController
// Considera cambiar esto a "/api/v1/publications" para consistencia
@RequestMapping("/publications")
@RequiredArgsConstructor
public class PublicationController {

	private final PublicationService service;

	@PostMapping
	public ResponseEntity<ApiResponse<PublicartionResponseDTO>> create(
			Authentication authentication,
			@Validated @RequestBody ControllerCreateDTO dto) {

		// Validar que la autenticación y el principal existan
		if (authentication == null || authentication.getPrincipal() == null) {
			return ResponseEntity.status(401)
					.body(ApiResponse.fail("Usuario no autenticado", 401));
		}

		// --- INICIO DE LA CORRECCIÓN ---
		Object principal = authentication.getPrincipal();
		UUID userUuid;

		// Verifica si el principal es tu objeto User
		if (principal instanceof User user) {
			userUuid = user.getId(); // Obtiene el UUID directamente
		} else {
			// Si no es un objeto User, intenta parsear el nombre (menos ideal)
            // Esto podría ser un fallback si cambias cómo configuras la autenticación
			System.err.println("Advertencia: El principal de Authentication no es un objeto User. Intentando obtener ID desde getName(). Principal: " + principal);
			try {
                // Intenta obtener el nombre (que podría ser el UUID si el principal fuera un UserDetails simple)
                String name = authentication.getName();
                if (name == null) {
                    throw new IllegalArgumentException("authentication.getName() devolvió null");
                }
 				userUuid = UUID.fromString(name);
			} catch (IllegalArgumentException e) {
				System.err.println("Error crítico: No se pudo obtener un UUID válido del usuario autenticado. Principal: " + principal + ", Error: " + e.getMessage());
				return ResponseEntity.status(500)
						.body(ApiResponse.error("Error interno al identificar al usuario", 500));
			}
		}

		String userId = userUuid.toString(); // Convierte el UUID a String para el servicio
        // --- FIN DE LA CORRECCIÓN ---

		System.out.println("=== CREATE PUBLICATION ===");
		System.out.println("userId from principal: " + userId); // Mensaje de log actualizado
		System.out.println("tempName: " + dto.getTempName());
		System.out.println("species: " + dto.getSpecies());
		System.out.println("photo length: " + (dto.getPhoto() != null ? dto.getPhoto().length() : 0));
		System.out.println("========================");

		// Llama al servicio con el String del UUID correcto
		return ResponseEntity.ok(service.create(userId, dto));
	}

    @GetMapping
	public ResponseEntity<ApiResponse<List<PublicartionResponseDTO>>> list(
			Authentication authentication) {

		// Si hay autenticación, devolver solo las del usuario
		if (authentication != null && authentication.getPrincipal() != null) {

            // --- INICIO CORRECCIÓN SIMILAR PARA GET ---
            Object principal = authentication.getPrincipal();
            UUID userUuid;

            if (principal instanceof User user) {
                userUuid = user.getId();
            } else {
                System.err.println("Advertencia GET: El principal de Authentication no es un objeto User. Intentando obtener ID desde getName(). Principal: " + principal);
                try {
                    String name = authentication.getName();
                     if (name == null) {
                         throw new IllegalArgumentException("authentication.getName() devolvió null");
                     }
                    userUuid = UUID.fromString(name);
                } catch (IllegalArgumentException e) {
                    System.err.println("Error crítico GET: No se pudo obtener un UUID válido del usuario autenticado. Principal: " + principal + ", Error: " + e.getMessage());
                     // Podrías devolver un error 500 o simplemente recurrir a listAll
                     return ResponseEntity.ok(service.listAll()); // Fallback a mostrar todas
                }
            }
             String userId = userUuid.toString();
            // --- FIN CORRECCIÓN SIMILAR PARA GET ---

			System.out.println("GET /publications - userId: " + userId);
			return ResponseEntity.ok(service.listByUserId(userId));
		}

		// Si no hay autenticación, devolver todas (para vista pública)
        System.out.println("GET /publications - No autenticado, devolviendo todas.");
		return ResponseEntity.ok(service.listAll());
	}


	// ... (resto de los métodos GET por ID, PUT, DELETE se mantienen igual) ...
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<PublicartionResponseDTO>> get(@PathVariable UUID id) {
		return ResponseEntity.ok(service.getById(id));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<PublicartionResponseDTO>> update(
			@PathVariable UUID id,
			@RequestBody PublicationUpdateDTO dto) {
		// Aquí no necesitas el userId del token, ya que operas sobre el {id} de la publicación
		return ResponseEntity.ok(service.update(id, dto));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Object>> delete(@PathVariable UUID id) {
        // Aquí tampoco necesitas el userId del token directamente
		return ResponseEntity.ok(service.delete(id));
	}
}