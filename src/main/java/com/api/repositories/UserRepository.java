package com.api.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.models.User;

// Es una interfaz que extiende de la interfaz proporcionada por Lombok
public interface UserRepository extends JpaRepository<User, UUID> {

}
