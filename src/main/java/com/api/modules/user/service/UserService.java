package com.api.modules.user.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.api.modules.user.dto.UserCreateDTO;
import com.api.modules.user.dto.UserResponseDTO;
import com.api.modules.user.dto.UserUpdateDTO;
import com.api.modules.user.mapper.UserMapper;
import com.api.modules.user.model.User;
import com.api.modules.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

//Aplica la lógica de negocio (validaciones, cálculos, etc).
@Service
@RequiredArgsConstructor // equivalente a @Autowired
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Obtener todos los usuarios
    public List<UserResponseDTO> getAll() {
        return userRepository.findAll().stream()
                .map(UserMapper::toResponseDTO)
                .toList();
    }

    // Obtener usuario por ID
    public UserResponseDTO getById(UUID id) {
        return userRepository.findById(id)
                .map(UserMapper::toResponseDTO)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    // Crear un nuevo usuario (mas adelante cifrado de contraseña)
    public UserResponseDTO create(UserCreateDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DataIntegrityViolationException("El correo electrónico ya está registrado.");
        }

        User user = UserMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        User saved = userRepository.save(user);
        return UserMapper.toResponseDTO(saved);
    }

    // Actualizar usuario existente
    public UserResponseDTO update(UUID id, UserUpdateDTO dto) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setName(dto.getName());
                    if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
                        user.setPassword(passwordEncoder.encode(dto.getPassword()));
                    }
                    user.setUpdatedAt(LocalDateTime.now());

                    User updated = userRepository.save(user);
                    return UserMapper.toResponseDTO(updated);
                })
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado para actualizar"));
    }

    // Eliminar usuario por ID
    public void delete(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado para eliminar");
        }
        userRepository.deleteById(id);
    }
}
