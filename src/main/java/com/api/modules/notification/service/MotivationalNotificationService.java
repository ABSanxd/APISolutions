package com.api.modules.notification.service;

import java.util.List;
import java.util.Random;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.api.modules.notification.model.Notification;
import com.api.modules.user.model.User;
import com.api.modules.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MotivationalNotificationService {

    private final EmailService emailService;
    private final UserRepository userRepository;

    // Lista de frases motivacionales 🐾
    private static final List<String> MOTIVATIONAL_MESSAGES = List.of(
            "🐾 ¡Hoy es un gran día para cuidar de tu peludo amigo!",
            "💪 No olvides tus retos del día, tu mascota confía en ti.",
            "🌟 ¡Sigue así! Tu dedicación está marcando la diferencia.",
            "🐶 ¡Un paseo más y tu mascota será la más feliz del mundo!",
            "💚 Cada cuidado cuenta. ¡Tu mascota te lo agradecerá!");

    /**
     * Enviar notificación motivacional aleatoria a todos los usuarios activos.
     * No se guardan en BD, solo se envían por correo o notificación interna.
     */
    public void sendMotivationalToAllUsers() {
        List<User> users = userRepository.findAll();
        Random random = new Random();

        for (User user : users) {
            String message = MOTIVATIONAL_MESSAGES.get(random.nextInt(MOTIVATIONAL_MESSAGES.size()));
            String title = "Woof Informa 🐾";

            Notification notification = new Notification();
            notification.setUser(user);
            notification.setTitle(title);
            notification.setMessage(message);
            notification.setActionUrl("/inicio"); // aquí decides a dónde llevarlo

            emailService.sendNotificationEmail(notification);
        }
    }

    /**
     * Scheduler automático:
     * Ejecuta el envío 2 veces al día:
     * - A las 09:00 a.m.
     * - A las 06:00 p.m.
     */
    @Scheduled(cron = "0 0 9,18 * * *", zone = "America/Lima")
    public void scheduledMotivationalNotifications() {
        sendMotivationalToAllUsers();
    }
}
