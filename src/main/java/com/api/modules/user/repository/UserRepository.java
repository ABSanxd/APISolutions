package com.api.modules.user.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.common.enums.Status;
import com.api.modules.user.model.User;

// Interactúa con la base de datos (JPA).
public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    List<User> findByStatusAndCreatedAtBefore(Status status, LocalDateTime fecha);

    List<User> findTop10ByOrderByUserXpDesc();
}
