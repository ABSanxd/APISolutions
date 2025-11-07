package com.api.modules.adoption.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import com.api.common.enums.Status;
import com.api.modules.publication.model.Publication;
import com.api.modules.user.model.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
// 1. Apuntamos a tu tabla "interested"
@Table(name = "interested") 
public class AdoptionRequest {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id; // Coincide con 'id' en tu diagrama

    // 2. Apuntamos a tu columna "id_user"
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false) 
    private User applicant;

    // 3. Apuntamos a tu columna "id_publication"
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_publication", nullable = false) 
    private Publication publication;

    // 4. Apuntamos a tu columna "status"
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false) 
    private Status status = Status.PENDIENTE;

    // 5. Apuntamos a tu columna "creation_date"
    @CreationTimestamp
    @Column(name = "creation_date", nullable = false, updatable = false) 
    private LocalDateTime createdAt;

    // (El campo "message" se queda eliminado, ya que no está en tu tabla)

    // Constructor actualizado (sin message)
    public AdoptionRequest(User applicant, Publication publication) {
        this.applicant = applicant;
        this.publication = publication;
    }
}