package com.api.modules.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.api.common.enums.Status;
import com.api.modules.auth.dto.LoginRequestDTO;
import com.api.modules.auth.dto.LoginResponseDTO;
import com.api.modules.auth.security.JwtUtils;
import com.api.modules.user.model.User;
import com.api.modules.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    public LoginResponseDTO login(LoginRequestDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario o contraseña incorrectos"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Usuario o contraseña incorrectos");
        }

        if (user.getStatus() != Status.ACTIVO) {
            throw new RuntimeException("Tu cuenta aún no ha sido verificada. Revisa tu correo.");
        }
        String token = jwtUtils.generateToken(user);

        return new LoginResponseDTO(token, user.getId(), user.getName(), user.getEmail());
    }
}