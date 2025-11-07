package com.api.modules.vaccine.model;

import java.time.LocalDate; 
import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "vaccine_dose")
public class VaccineDose {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(name = "application_date", nullable = false)
    private LocalDate applicationDate; 

    @Column(nullable = false)
    private boolean applied = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vaccine", nullable = false)
    @ToString.Exclude
    private Vaccine vaccine;
}