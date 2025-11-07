package com.api.modules.user.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.api.common.enums.CodePurpose;
import com.api.common.enums.Status;
import com.api.modules.notification.service.EmailService;
import com.api.modules.user.model.User;
import com.api.modules.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserVerificationService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    // Verificación del código de correo
    private void validateCode(User user, String code, CodePurpose purpose) {
        if (user.getVerificationCode() == null || user.getCodeExpiresAt() == null) {
            throw new RuntimeException("No se ha solicitado código.");
        }

        if (!purpose.equals(user.getCodePurpose())) {
            throw new RuntimeException("El código no corresponde a esta acción.");
        }

        if (user.getCodeExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("El código ha expirado. Solicita uno nuevo.");
        }

        if (!user.getVerificationCode().equals(code)) {
            throw new RuntimeException("Código incorrecto.");
        }
    }

    // Verificar correo
    public void verifyEmailCode(String email, String code) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (user.getStatus() == Status.ACTIVO) {
            throw new RuntimeException("El correo ya fue verificado.");
        }

        validateCode(user, code, CodePurpose.VERIFICATION);

        user.setStatus(Status.ACTIVO);
        user.setVerificationCode(null);
        user.setCodeExpiresAt(null);
        user.setCodePurpose(null);
        userRepository.save(user);
    }

    // Restablecer contraseñas
    public void resetPassword(String email, String code, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        validateCode(user, code, CodePurpose.RESET_PASSWORD);

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setVerificationCode(null);
        user.setCodeExpiresAt(null);
        user.setCodePurpose(null);
        userRepository.save(user);
    }

    public void resendVerificationCode(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (user.getStatus() == Status.ACTIVO) {
            throw new RuntimeException("El correo ya fue verificado.");
        }

        emailService.enviarCodigoVerificacion(user);
    }

    public void sendPasswordResetCode(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        emailService.enviarCodigoRecuperacion(user);
    }
}
