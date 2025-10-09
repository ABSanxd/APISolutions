package com.api.modules.user.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.common.response.ApiResponse;
import com.api.modules.user.dto.UserRequestDTO;
import com.api.modules.user.dto.UserResponseDTO;
import com.api.modules.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

// Recibe las peticiones HTTP (REST). Solo coordina. 
// No hace lógica.
@RestController
@RequiredArgsConstructor // Singlenton Automático
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAll();
        return ResponseEntity.ok(ApiResponse.success(users, "Usuarios obtenidos correctamente"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUserById(@PathVariable UUID id) {
        UserResponseDTO user = userService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(user, "Usuario encontrado"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDTO>> createUser(@Valid @RequestBody UserRequestDTO userDto) {
        UserResponseDTO createdUser = userService.create(userDto);
        return ResponseEntity.ok(ApiResponse.success(createdUser, "Usuario creado exitosamente"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateUser(
            @PathVariable UUID id,
            @Valid @RequestBody UserRequestDTO userDto) {
        UserResponseDTO updatedUser = userService.update(id, userDto);
        return ResponseEntity.ok(ApiResponse.success(updatedUser, "Usuario actualizado correctamente"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Usuario eliminado correctamente"));
    }
}
