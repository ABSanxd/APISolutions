package com.api.modules.user.factory;

import java.time.LocalDateTime;

import com.api.common.enums.Status;
import com.api.common.enums.UserLevel;
import com.api.modules.user.model.User;

// Crea objetos listos para usar (inicializados)
public class UserFactory {
    public static User createUser(String name, String email, String password) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setMaxPets(2); // valor predeterminado
        user.setUserLevel(UserLevel.BRONCE);
        user.setUserXp(0);
        user.setStatus(Status.ACTIVO);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return user;
    }
}
