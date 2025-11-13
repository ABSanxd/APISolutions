package com.api.modules.petchallenge.repository;

import com.api.modules.petchallenge.models.PetChallenge;

import org.springframework.data.jpa.repository.JpaRepository;

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

        // Contar cuantas veces una mascota completó un reto especifico-sin limite de
        // tiempo(TOTAL)
        long countByPetIdAndChallengeId(UUID petId, UUID challengeId);

        // Contar cuántas veces se completó un reto en un periodo (SEMANAL/MENSUAL)
        long countByPetIdAndChallengeIdAndCreatedAtBetween(
                        UUID petId,
                        UUID challengeId,
                        LocalDateTime start,
                        LocalDateTime end);

}
