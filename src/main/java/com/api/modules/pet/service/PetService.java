package com.api.modules.pet.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.api.common.enums.Status;
import com.api.modules.pet.dto.PetCreateDTO;
import com.api.modules.pet.dto.PetResponseDTO;
import com.api.modules.pet.dto.PetUpdateDTO;
import com.api.modules.pet.mapper.PetMapper;
import com.api.modules.pet.model.Pet;
import com.api.modules.pet.repository.PetRepository;
import com.api.modules.user.model.User; 
import com.api.modules.user.repository.UserRepository; 

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PetService {
    
    private final PetRepository petRepository;
    private final UserRepository userRepository; 

    //mascotas por usuario (Sin cambios, JPA lo maneja)
    public List<PetResponseDTO> getAllPetsByUser(UUID userId) {
        return petRepository.findByUserIdAndStatus(userId, Status.ACTIVO).stream()
                .map(PetMapper::toResponseDTO)
                .toList();
    }

    //obtener mascota por id (Sin cambios, JPA lo maneja)
    public PetResponseDTO getPetById(UUID id, UUID userId) {
        return petRepository.findByIdAndUserId(id, userId)
                .map(PetMapper::toResponseDTO)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada o no pertenece al usuario"));
    }

    public PetResponseDTO createPet(PetCreateDTO dto, UUID userId) {
        long petCount = petRepository.countByUserIdAndStatus(userId, Status.ACTIVO);
        if (petCount >= 2) { // maxPets por defecto es 2
            throw new RuntimeException("Has alcanzado el límite de mascotas permitidas");
        }

        // 1. Buscar la entidad User completa
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado para crear la mascota"));

        // 2. Pasar el objeto User completo al mapper
        Pet pet = PetMapper.toEntity(dto, user); 
        
        Pet savedPet = petRepository.save(pet);
        return PetMapper.toResponseDTO(savedPet);
    }

    // Actualizar mascota existente (Sin cambios, JPA lo maneja)
    public PetResponseDTO updatePet(UUID id, PetUpdateDTO dto, UUID userId) {
        return petRepository.findByIdAndUserId(id, userId)
                .map(pet -> {
                    PetMapper.updateEntity(pet, dto);
                    pet.setUpdatedIn(LocalDateTime.now());
                    
                    Pet updatedPet = petRepository.save(pet);
                    return PetMapper.toResponseDTO(updatedPet);
                })
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada para actualizar"));
    }

    // Eliminar mascota (Sin cambios, JPA lo maneja)
    public void deletePet(UUID id, UUID userId) {
        Pet pet = petRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada para eliminar"));
        
        pet.setStatus(Status.INACTIVO);
        pet.setUpdatedIn(LocalDateTime.now());
        petRepository.save(pet);
    }

    // (Sin cambios, JPA lo maneja)
    public long countActivePets(UUID userId) {
        return petRepository.countByUserIdAndStatus(userId, Status.ACTIVO);
    }
}