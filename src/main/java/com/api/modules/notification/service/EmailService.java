package com.api.modules.notification.service;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.api.common.enums.CodePurpose;
import com.api.modules.notification.model.Notification;
import com.api.modules.user.model.User;
import com.api.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final UserRepository userRepository;
    private final JavaMailSender mailSender;

    private void enviarCodigo(User user, CodePurpose purpose, String subject, String cuerpo) {
        String code = String.format("%06d", new Random().nextInt(999999));
        LocalDateTime expiracion = LocalDateTime.now().plusMinutes(5);

        user.setVerificationCode(code);
        user.setCodeExpiresAt(expiracion);
        user.setCodePurpose(purpose);
        userRepository.save(user);

        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(user.getEmail());
        mensaje.setSubject(subject);
        mensaje.setText(String.format(cuerpo, user.getName(), code));
        mailSender.send(mensaje);
    }

    public void enviarCodigoVerificacion(User user) {
        enviarCodigo(
                user,
                CodePurpose.VERIFICATION,
                "Código de verificación - Woof Berlin",
                "¡Hola %s! 🐾\n\nTu código de verificación es: %s\nEste código expirará en 5 minutos.\n\nSi no solicitaste esta verificación, ignora este mensaje.");
    }

    public void enviarCodigoRecuperacion(User user) {
        enviarCodigo(
                user,
                CodePurpose.RESET_PASSWORD,
                "Recuperación de contraseña - Woof Berlin",
                "¡Hola %s! 🐾\n\nTu código para restablecer la contraseña es: %s\nEste código expirará en 5 minutos.\n\nSi no solicitaste esta recuperación, ignora este mensaje.");
    }

    public void sendNotificationEmail(Notification notification) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(notification.getUser().getEmail());
        mensaje.setSubject(notification.getTitle());
        mensaje.setText(notification.getMessage());
        mailSender.send(mensaje);
    }
}