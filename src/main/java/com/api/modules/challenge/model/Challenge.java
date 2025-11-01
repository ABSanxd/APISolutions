package com.api.modules.challenge.model;
import jakarta.persistence.*;
import com.api.common.enums.Category;
import com.api.common.enums.Frequency;
import lombok.Data;
import com.api.modules.petchallenge.models.PetChallenge;
import com.api.modules.challengerequirements.model.ChallengeRequirements;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

@Data
@Entity
@Table(name = "challenge")

public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "frequency", nullable = false, length = 50)
    private Frequency frequency;

    @Column(name= "points", nullable = false)
    private Integer points;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 100)
    private Category category;

    @Column(name = "image", columnDefinition = "TEXT")
    private String image;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = true)
    private LocalDate updatedAt;

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PetChallenge> petChallenges;

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChallengeRequirements> requirements;
    
}
