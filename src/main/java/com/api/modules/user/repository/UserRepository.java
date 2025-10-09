package com.api.modules.user.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.modules.user.model.User;

// Interactúa con la base de datos (JPA).
public interface UserRepository extends JpaRepository<User, UUID> {

}
