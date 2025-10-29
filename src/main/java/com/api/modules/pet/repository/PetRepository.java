package com.api.modules.pet.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.common.enums.Status;
import com.api.modules.pet.model.Pet;

@Repository
public interface PetRepository extends JpaRepository<Pet, UUID> {
    
    List<Pet> findByUserId(UUID userId);
    
    List<Pet> findByUserIdAndStatus(UUID userId, Status status);
    
    Optional<Pet> findByIdAndUserId(UUID id, UUID userId);
    
    boolean existsByUserId(UUID userId);
    
    long countByUserIdAndStatus(UUID userId, Status status);
}