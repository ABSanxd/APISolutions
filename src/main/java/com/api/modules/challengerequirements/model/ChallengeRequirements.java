package com.api.modules.challengerequirements.model;
import com.api.modules.challenge.model.Challenge;
import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

@Data
@Entity
@Table(name = "challenge_requirements")

public class ChallengeRequirements {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "achievement_id ", nullable = false)
    private UUID achievementId; // logro_id (FK)    

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge; // desafio_id (FK)
    

    @Column(name = "repetitions")
    private Integer repetitions; 

}
