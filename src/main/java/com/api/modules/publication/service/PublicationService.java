package com.api.modules.publication.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.api.common.response.ApiResponse;
import com.api.modules.publication.dto.PublicationCreateDTO;
import com.api.modules.publication.dto.PublicationResponseDTO;
import com.api.modules.publication.dto.PublicationUpdateDTO;
import com.api.modules.publication.mapper.PublicationMapper;
import com.api.modules.publication.model.Publication;
import com.api.modules.publication.repository.PublicationRepository;
import com.api.modules.user.model.User;
import com.api.modules.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PublicationService {
    

	private final PublicationRepository repository;
	private final UserRepository userRepository;

	public ApiResponse<PublicationResponseDTO> create(PublicationCreateDTO dto) {
		return ApiResponse.fail("Use header X-User-Id para identificar al usuario", 400);
	}

	public ApiResponse<PublicationResponseDTO> create(String userIdHeader, PublicationCreateDTO dto) {
		java.util.UUID userUuid;
		try {
			userUuid = java.util.UUID.fromString(userIdHeader);
		} catch (Exception ex) {
			return ApiResponse.fail("X-User-Id inválido: debe ser UUID con formato estándar (36 caracteres)", 400);
		}

		Optional<User> ou = userRepository.findById(userUuid);
		if (ou.isEmpty())
			return ApiResponse.fail("Usuario no encontrado", 404);

		Publication p = PublicationMapper.toEntity(dto, ou.get());
		Publication saved = repository.save(p);
		return ApiResponse.success(PublicationMapper.toResponseDTO(saved));
	}

	public ApiResponse<List<PublicationResponseDTO>> listAll() {
		List<Publication> list = repository.findAll();
		return ApiResponse.success(list.stream().map(PublicationMapper::toResponseDTO).collect(Collectors.toList()));
	}

	public ApiResponse<PublicationResponseDTO> getById(UUID id) {
		Optional<Publication> op = repository.findById(id);
		if (op.isEmpty())
			return ApiResponse.fail("Publicacion no encontrada", 404);
		return ApiResponse.success(PublicationMapper.toResponseDTO(op.get()));
	}

	public ApiResponse<PublicationResponseDTO> update(UUID id, PublicationUpdateDTO dto) {
		Optional<Publication> op = repository.findById(id);
		if (op.isEmpty())
			return ApiResponse.fail("Publicacion no encontrada", 404);

		Publication p = op.get();
		PublicationMapper.updateEntity(p, dto);
		p.setUpdateDate(java.time.LocalDate.now());
		Publication saved = repository.save(p);
		return ApiResponse.success(PublicationMapper.toResponseDTO(saved));
	}

	public ApiResponse<Object> delete(UUID id) {
		Optional<Publication> op = repository.findById(id);
		if (op.isEmpty())
			return ApiResponse.fail("Publicacion no encontrada", 404);
		repository.deleteById(id);
		return ApiResponse.success(null, "Eliminado");
	}
}