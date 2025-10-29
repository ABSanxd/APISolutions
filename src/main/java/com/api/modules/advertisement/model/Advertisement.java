package com.api.modules.advertisement.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import com.api.common.enums.PriorityAd;
import com.api.common.enums.ZoneAd;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.api.common.enums.Status;
import org.hibernate.annotations.Type;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "advertisement")
public class Advertisement {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;// por si es largo xd

    private String link; // sitio web

    @Column(columnDefinition = "TEXT") // para + varchar(255)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ZoneAd zone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PriorityAd priority;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private Boolean isRenewable = false;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private ContactDetails contact;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.ACTIVO;

    @CreationTimestamp // automatico
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp // Se actualiza solo en cada update
    @Column(nullable = false)
    private LocalDateTime updatedAt;

}
