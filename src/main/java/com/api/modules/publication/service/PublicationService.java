package com.api.modules.publication.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.api.common.response.ApiResponse;
import com.api.modules.publication.dto.ControllerCreateDTO;
import com.api.modules.publication.dto.PublicartionResponseDTO;
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

	public ApiResponse<PublicartionResponseDTO> create(String userIdStr, ControllerCreateDTO dto) {
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
		return ApiResponse.success(PublicationMapper.toResponseDTO(saved));
	}

	public ApiResponse<List<PublicartionResponseDTO>> listAll() {
		List<Publication> list = repository.findAll();
		return ApiResponse.success(list.stream()
				.map(PublicationMapper::toResponseDTO)
				.collect(Collectors.toList()));
	}

	public ApiResponse<List<PublicartionResponseDTO>> listByUserId(String userIdStr) {
		try {
			UUID userId = UUID.fromString(userIdStr);
			List<Publication> list = repository.findByUserId(userId);
			return ApiResponse.success(list.stream()
					.map(PublicationMapper::toResponseDTO)
					.collect(Collectors.toList()));
		} catch (Exception ex) {
			return ApiResponse.fail("UserId inválido", 400);
		}
	}

	public ApiResponse<PublicartionResponseDTO> getById(UUID id) {
		Optional<Publication> op = repository.findById(id);
		if (op.isEmpty())
			return ApiResponse.fail("Publicacion no encontrada", 404);
		return ApiResponse.success(PublicationMapper.toResponseDTO(op.get()));
	}

	public ApiResponse<PublicartionResponseDTO> update(UUID id, PublicationUpdateDTO dto) {
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