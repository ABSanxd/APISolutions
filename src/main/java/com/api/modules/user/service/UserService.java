package com.api.modules.user.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.api.modules.user.dto.UserRequestDTO;
import com.api.modules.user.dto.UserResponseDTO;
import com.api.modules.user.factory.UserFactory;
import com.api.modules.user.mapper.UserMapper;
import com.api.modules.user.model.User;
import com.api.modules.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

//Aplica la lógica de negocio (validaciones, cálculos, etc).
@Service
@RequiredArgsConstructor // equivalente a @Autowired
public class UserService {
    private final UserRepository userRepository;

    // Obtener todos los usuarios
    public List<UserResponseDTO> getAll() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Obtener usuario por ID
    public UserResponseDTO getById(UUID id) {
        return userRepository.findById(id)
                .map(UserMapper::toResponseDTO)
                .orElse(null);
    }

    // Crear un nuevo usuario (mas adelante cifrado de contraseña)
    public UserResponseDTO create(UserRequestDTO dto) {
        // Convertimos el DTO a entidad :,D
        User user = UserMapper.toEntity(dto);
        user = UserFactory.createUser(
                user.getName(),
                user.getEmail(),
                user.getPassword());
        // Guardamos en la BD
        User saved = userRepository.save(user);
        // Retornamos la respuesta como DTO
        return UserMapper.toResponseDTO(saved);
    }

    // Actualizar usuario existente
    public UserResponseDTO update(UUID id, UserRequestDTO dto) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setName(dto.getName());
                    user.setEmail(dto.getEmail());
                    user.setPassword(dto.getPassword());
                    user.setUpdatedAt(LocalDateTime.now());
                    User updated = userRepository.save(user);
                    return UserMapper.toResponseDTO(updated);
                })
                .orElse(null);
    }

    // Eliminar usuario por ID
    public void delete(UUID id) {
        userRepository.deleteById(id);
    }
}
