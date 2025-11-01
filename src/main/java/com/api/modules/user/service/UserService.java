package com.api.modules.user.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
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
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toResponseDTO)
                .toList();
    }

    // Obtener usuario por ID
    public UserResponseDTO getUserById(UUID id) {
        return userRepository.findById(id)
                .map(UserMapper::toResponseDTO)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    // Crear un nuevo usuario (mas adelante cifrado de contraseña)
    public UserResponseDTO createUser(UserCreateDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DataIntegrityViolationException("No se pudo completar el registro. Verifica tus datos");
        }
        validarEdadMinima(dto.getBirthDate());

        User user = UserMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        User savedUser = userRepository.save(user);
        return UserMapper.toResponseDTO(savedUser);
    }

    // Actualizar usuario existente
    public UserResponseDTO updateUser(UUID id, UserUpdateDTO dto) {
        return userRepository.findById(id)
                .map(user -> {
                    UserMapper.updateEntity(user, dto);
                    if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
                        user.setPassword(passwordEncoder.encode(dto.getPassword()));
                    }
                    user.setUpdatedAt(LocalDateTime.now());

                    User updatedUser = userRepository.save(user);
                    return UserMapper.toResponseDTO(updatedUser);
                })
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado para actualizar"));
    }

    // Eliminar usuario por ID
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado para eliminar");
        }
        userRepository.deleteById(id);
    }

    private void validarEdadMinima(LocalDate birthDate) {
        if (birthDate == null) {
            throw new IllegalArgumentException("La fecha de nacimiento es obligatoria.");
        }

        LocalDate hoy = LocalDate.now();
        Period edad = Period.between(birthDate, hoy);

        if (edad.getYears() < 9) {
            throw new IllegalArgumentException("Debes tener al menos 9 años para registrarte.");
        }
    }
}
