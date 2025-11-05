package com.api.modules.user.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.api.common.enums.Status;
import com.api.common.enums.UserLevel;
import com.api.modules.pet.model.Pet;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Data // Parte de la Data
@Entity // Es una entidad
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password; // BCrypt

    @Column(nullable = false)
    private int maxPets = 2;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserLevel userLevel = UserLevel.BRONCE;

    @Column(nullable = false)
    private int userXp = 0;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private String province;

    @Column(nullable = false)
    private String district;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.ACTIVO;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user")
    @ToString.Exclude 
    private List<Pet> pets;
}
