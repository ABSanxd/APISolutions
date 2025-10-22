package com.api.modules.publication.model;

import java.time.LocalDate;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.api.common.enums.Status;
import com.api.common.enums.species;
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

@Data
@Entity
@Table(name = "publication")
public class Publication {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(name = "temp_name", nullable = false)
    private String tempName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private species species;

    @Column(name = "approx_age", nullable = false)
    private String approxAge;

    @Column(nullable = false)
    private String photo;

    @Column(nullable = false)
    private String description;

    // contact stored as JSON in Postgres
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    private Map<String, Object> contact;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDIENTE;

    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate = LocalDate.now();

    @Column(name = "update_date")
    private LocalDate updateDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}

