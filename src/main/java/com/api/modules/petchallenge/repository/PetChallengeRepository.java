package com.api.modules.petchallenge.repository;

import com.api.modules.petchallenge.models.PetChallenge;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

import java.util.List;

public interface PetChallengeRepository extends JpaRepository<PetChallenge, UUID> {
    //obtener todos los avances de una mascota (historial de retos completados)
    List<PetChallenge> findByPetId(UUID petId);

    //Contar cuantas veces una mascota completó un reto especifico
    long countByPetIdAndChallengeId(UUID petId, UUID challengeId);


}
