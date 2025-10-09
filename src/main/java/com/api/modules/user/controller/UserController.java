package com.api.modules.user.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.modules.user.dto.UserRequestDTO;
import com.api.modules.user.dto.UserResponseDTO;
import com.api.modules.user.service.UserService;

import lombok.RequiredArgsConstructor;

// Recibe las peticiones HTTP (REST). Solo coordina. 
// No hace lógica.
@RestController
@RequiredArgsConstructor // Singlenton Automático
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public UserResponseDTO getUserById(@PathVariable UUID id) {
        return userService.getById(id);
    }

    @PostMapping
    public UserResponseDTO createUser(@RequestBody UserRequestDTO userDto) {
        return userService.create(userDto);
    }

    @PutMapping("/{id}")
    public UserResponseDTO updateUser(@PathVariable UUID id, @RequestBody UserRequestDTO userDto) {
        return userService.update(id, userDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable UUID id) {
        userService.delete(id);
    }
}
