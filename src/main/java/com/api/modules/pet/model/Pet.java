package com.api.modules.pet.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.api.common.enums.PetLevel;
import com.api.common.enums.Species;
import com.api.common.enums.Status;
import com.api.modules.user.model.User;
import com.api.modules.vaccine.model.Vaccine;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType; 
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn; 
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString; 

@Data
@Entity
@Table(name = "pet")
public class Pet {
    
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "user_id", nullable = false) 
    @ToString.Exclude 
    private User user;
    
    @Column(name = "name", nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "species", nullable = false)
    private Species especie;

    @Enumerated(EnumType.STRING)
    @Column(name = "pet_level", nullable = false)
    private PetLevel nivel = PetLevel.NOVATO;

    @Column(name = "pet_xp", nullable = false)
    private Integer petXp = 0;

    @Column(name = "breed")
    private String breed;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "pet_weight")
    private BigDecimal petWeight;

    // Le decimos a JPA que use el tipo de dato "TEXT" en la BD
    // en lugar de VARCHAR(255), para que quepan los Base64
    @Column(name = "photo", columnDefinition = "TEXT")
    private String photo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.ACTIVO;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_in")
    private LocalDateTime updatedIn;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Vaccine> vaccines;
}