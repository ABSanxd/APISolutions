package com.api.modules.vaccine.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.api.modules.vaccine.model.Vaccine;

@Repository
public interface VaccineRepository extends JpaRepository<Vaccine, UUID> {

    // Busca todas las vacunas de una mascota
    List<Vaccine> findByPetId(UUID petId);

    // Busca una vacuna específica que pertenezca a una mascota (para seguridad)
    @Query("SELECT v FROM Vaccine v WHERE v.id = :vaccineId AND v.pet.id = :petId")
    Optional<Vaccine> findByIdAndPetId(UUID vaccineId, UUID petId);
}