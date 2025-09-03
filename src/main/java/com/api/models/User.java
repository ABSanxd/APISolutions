package com.api.models;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data //Parte de la Data
@Entity //Es una entidad
@Table(name = "users")
public class User {
    @Id  
    @GeneratedValue()
    @UuidGenerator
    private UUID id;
    private String username;
    private String password;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
