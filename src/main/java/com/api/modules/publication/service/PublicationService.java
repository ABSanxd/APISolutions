package com.api.modules.publication.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.api.common.enums.Status;
import com.api.common.response.ApiResponse;
import com.api.modules.publication.dto.PublicationCreateDTO;
import com.api.modules.publication.dto.PublicationResponseDTO;
import com.api.modules.publication.dto.PublicationUpdateDTO;
import com.api.modules.publication.mapper.PublicationMapper;
import com.api.modules.publication.model.Publication;
import com.api.modules.publication.repository.PublicationRepository;
import com.api.modules.user.model.User;
import com.api.modules.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PublicationService {

	private final PublicationRepository repository;
	private final UserRepository userRepository;

	public ApiResponse<PublicationResponseDTO> create(String userIdStr, PublicationCreateDTO dto) {
		// ... (sin cambios) ...
		UUID userUuid;
		try {
			userUuid = UUID.fromString(userIdStr);
		} catch (Exception ex) {
			return ApiResponse.fail("UserId inválido: debe ser UUID válido", 400);
		}

		Optional<User> ou = userRepository.findById(userUuid);
		if (ou.isEmpty())
			return ApiResponse.fail("Usuario no encontrado", 404);

		Publication p = PublicationMapper.toEntity(dto, ou.get());
		Publication saved = repository.save(p);
		return ApiResponse.success(PublicationMapper.toResponseDTO(saved)); // <-- Se usa el mapper base
	}
	
	public ApiResponse<List<PublicationResponseDTO>> listAvailable(String userIdStr) {
		UUID userUuid;
		try {
			userUuid = UUID.fromString(userIdStr);
		} catch (Exception ex) {
			return ApiResponse.fail("UserId inválido: debe ser UUID válido", 400);
		}

		List<Publication> list = repository.findByStatusAndUser_IdNot(Status.ACTIVO, userUuid);
		
		// --- INICIO DE CAMBIOS ---
		// Usamos el mapper que calcula 'likedByMe'
		return ApiResponse.success(list.stream()
				.map(p -> PublicationMapper.toResponseDTO(p, userUuid))
				.collect(Collectors.toList()));
		// --- FIN DE CAMBIOS ---
	}

	public ApiResponse<List<PublicationResponseDTO>> listAll() {
		List<Publication> list = repository.findAll();
		return ApiResponse.success(list.stream()
				.map(PublicationMapper::toResponseDTO) // <-- Se usa el mapper base
				.collect(Collectors.toList()));
	}

	public ApiResponse<List<PublicationResponseDTO>> listByUserId(String userIdStr) {
		try {
			UUID userId = UUID.fromString(userIdStr);
			List<Publication> list = repository.findByUserId(userId);
			// --- INICIO DE CAMBIOS ---
			// El usuario está viendo su propia lista, así que 'likedByMe' es relevante
			return ApiResponse.success(list.stream()
					.map(p -> PublicationMapper.toResponseDTO(p, userId))
					.collect(Collectors.toList()));
			// --- FIN DE CAMBIOS ---
		} catch (Exception ex) {
			return ApiResponse.fail("UserId inválido", 400);
		}
	}

	// --- INICIO DE CAMBIOS ---
	// AHORA REQUIERE EL ID DEL USUARIO ACTUAL para calcular 'likedByMe'
	public ApiResponse<PublicationResponseDTO> getById(UUID id, UUID currentUserId) {
		Optional<Publication> op = repository.findById(id);
		if (op.isEmpty())
			return ApiResponse.fail("Publicacion no encontrada", 404);
		
		// Usamos el mapper que calcula 'likedByMe'
		return ApiResponse.success(PublicationMapper.toResponseDTO(op.get(), currentUserId));
	}
	// --- FIN DE CAMBIOS ---

	public ApiResponse<PublicationResponseDTO> update(UUID id, PublicationUpdateDTO dto) {
		// ... (sin cambios) ...
		Optional<Publication> op = repository.findById(id);
		if (op.isEmpty())
			return ApiResponse.fail("Publicacion no encontrada", 404);

		Publication p = op.get();
		PublicationMapper.updateEntity(p, dto);
		p.setUpdateDate(java.time.LocalDate.now());
		Publication saved = repository.save(p);
		return ApiResponse.success(PublicationMapper.toResponseDTO(saved)); // <-- Se usa el mapper base
	}

	public ApiResponse<Object> delete(UUID id) {
		// ... (sin cambios) ...
		Optional<Publication> op = repository.findById(id);
		if (op.isEmpty())
			return ApiResponse.fail("Publicacion no encontrada", 404);
		repository.deleteById(id);
		return ApiResponse.success(null, "Eliminado");
	}

	// --- INICIO DE CAMBIOS ---
	// LÓGICA DE LIKE/UNLIKE
	@Transactional
	public ApiResponse<PublicationResponseDTO> toggleLikePublication(UUID id, User user) {
		Optional<Publication> op = repository.findById(id);
		if (op.isEmpty())
			return ApiResponse.fail("Publicacion no encontrada", 404);

		Publication p = op.get();
		
		// Lógica de Toggle:
		// Si el 'Set' de 'likedBy' ya contiene al usuario, lo quitamos (unlike).
		// Si no lo contiene, lo añadimos (like).
		if (p.getLikedBy().contains(user)) {
			p.getLikedBy().remove(user);
		} else {
			p.getLikedBy().add(user);
		}
		
		Publication saved = repository.save(p);
		// Devolvemos el DTO calculado para ESTE usuario (likedByMe será correcto)
		return ApiResponse.success(PublicationMapper.toResponseDTO(saved, user.getId()));
	}

	@Transactional
	public ApiResponse<PublicationResponseDTO> sharePublication(UUID id) {
		// ... (sin cambios, este método estaba bien) ...
		Optional<Publication> op = repository.findById(id);
		if (op.isEmpty())
			return ApiResponse.fail("Publicacion no encontrada", 404);

		Publication p = op.get();
		p.setShared(p.getShared() + 1); // Incrementar shares
		Publication saved = repository.save(p);
		return ApiResponse.success(PublicationMapper.toResponseDTO(saved)); // <-- Se usa el mapper base
	}
	// --- FIN DE CAMBIOS ---
}