package com.api.modules.pet.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.api.common.enums.PetLevel;
import com.api.common.enums.Species;
import com.api.common.enums.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import com.api.modules.petchallenge.models.PetChallenge;
import jakarta.persistence.CascadeType;
@Data
@Entity
@Table(name = "pet")
public class Pet {
    
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "name", nullable = false)
    private String nombre;

    @Column(name = "species", nullable = false)
    @Enumerated(EnumType.STRING)
    private Species especie;

    @Column(name = "pet_level", nullable = false)
    @Enumerated(EnumType.STRING)
    private PetLevel nivel = PetLevel.NOVATO;

    @Column(name = "pet_xp", nullable = false)
    private Integer petXp = 0;

    @Column(name = "breed")
    private String breed;

    @Column(name = "pet_age")
    private Integer petAge;

    @Column(name = "pet_weight")
    private BigDecimal petWeight;

    @Column(name = "photo")
    private String photo;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVO;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_in")
    private LocalDateTime updatedIn;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PetChallenge> petChallenges;
}