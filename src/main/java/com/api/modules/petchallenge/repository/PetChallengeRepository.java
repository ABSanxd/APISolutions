package com.api.modules.petchallenge.repository;

import com.api.modules.petchallenge.models.PetChallenge;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;
import java.time.LocalDateTime;
import java.util.List;

public interface PetChallengeRepository extends JpaRepository<PetChallenge, UUID> {

    // obtener todos los retos completados ordenado por más reciente
    List<PetChallenge> findByPetIdOrderByCreatedAtDesc(UUID petId);

    // obtener retos completados en un rango de fechas (usado para Hoy/Semana)
    List<PetChallenge> findByPetIdAndCreatedAtBetween(UUID petId, LocalDateTime start, LocalDateTime end);

    // obtener todos los avances de una mascota (historial de retos completados)
    List<PetChallenge> findByPetId(UUID petId);

    // // Contar cuántos retos ha completado una mascota
    long countByPetId(UUID petId);

    // Contar cuantas veces una mascota completó un reto especifico
    long countByPetIdAndChallengeId(UUID petId, UUID challengeId);

    // para verificar logros
    // Contar cuántas veces completó un reto en un rango de fechas
    @Query("SELECT COUNT(pc) FROM PetChallenge pc WHERE pc.pet.id = :petId " +
            "AND pc.challenge.id = :challengeId " +
            "AND pc.createdAt BETWEEN :start AND :end")
    long countByChallengeInPeriod(
            @Param("petId") UUID petId,
            @Param("challengeId") UUID challengeId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

}
