package com.api.modules.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.common.response.ApiResponse;
import com.api.modules.auth.dto.LoginRequestDTO;
import com.api.modules.auth.dto.LoginResponseDTO;
import com.api.modules.auth.service.AuthService;
import com.api.modules.user.dto.UserCreateDTO;
import com.api.modules.user.dto.UserResponseDTO;
import com.api.modules.user.service.UserService;
import com.api.modules.user.service.UserVerificationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final UserVerificationService userVerificationService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@Valid @RequestBody LoginRequestDTO request) {
        LoginResponseDTO response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Login exitoso"));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponseDTO>> registerUser(@Valid @RequestBody UserCreateDTO userDto) {
        UserResponseDTO createdUser = userService.createUser(userDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(createdUser, "Usuario registrado exitosamente"));
    }

    @PostMapping("/verify-code")
    public ResponseEntity<ApiResponse<String>> verifyCode(
            @RequestParam String email,
            @RequestParam String code) {

        userVerificationService.verifyEmailCode(email, code);
        return ResponseEntity.ok(ApiResponse.success("Cuenta verificada correctamente", "Verificación exitosa"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(
            @RequestParam String email,
            @RequestParam String code,
            @RequestParam String newPassword) {
        userVerificationService.resetPassword(email, code, newPassword);
        return ResponseEntity
                .ok(ApiResponse.success("Contraseña actualizada correctamente.", "Restablecimiento exitoso"));
    }

    @PostMapping("/resend-code")
    public ResponseEntity<ApiResponse<String>> resendVerificationCode(@RequestParam String email) {
        userVerificationService.resendVerificationCode(email);
        return ResponseEntity.ok(ApiResponse.success(
                "Se ha enviado un nuevo código de verificación.",
                "Código reenviado"));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@RequestParam String email) {
        userVerificationService.sendPasswordResetCode(email);
        return ResponseEntity.ok(ApiResponse.success(
                "Se ha enviado un código para restablecer la contraseña a tu correo.",
                "Código de recuperación enviado"));
    }
}