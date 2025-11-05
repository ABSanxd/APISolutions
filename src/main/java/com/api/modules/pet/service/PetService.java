package com.api.modules.pet.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.api.common.enums.PetLevel;
import com.api.common.enums.Status;
import com.api.common.exception.ResourceNotFoundException;
import com.api.modules.pet.dto.PetCreateDTO;
import com.api.modules.pet.dto.PetResponseDTO;
import com.api.modules.pet.dto.PetUpdateDTO;
import com.api.modules.pet.mapper.PetMapper;
import com.api.modules.pet.model.Pet;
import com.api.modules.pet.repository.PetRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;

    // mascotas por usuario
    public List<PetResponseDTO> getAllPetsByUser(UUID userId) {
        return petRepository.findByUserIdAndStatus(userId, Status.ACTIVO).stream()
                .map(PetMapper::toResponseDTO)
                .toList();
    }

    // obtener mascota por id
    public PetResponseDTO getPetById(UUID id, UUID userId) {
        return petRepository.findByIdAndUserId(id, userId)
                .map(PetMapper::toResponseDTO)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada o no pertenece al usuario"));
    }

    public PetResponseDTO createPet(PetCreateDTO dto, UUID userId) {
        // validar el limite de mascotas (pero se puede cambiar o borrar)
        long petCount = petRepository.countByUserIdAndStatus(userId, Status.ACTIVO);
        if (petCount >= 2) { // maxPets por defecto es 2
            throw new RuntimeException("Has alcanzado el límite de mascotas permitidas");
        }

        Pet pet = PetMapper.toEntity(dto, userId);
        Pet savedPet = petRepository.save(pet);
        return PetMapper.toResponseDTO(savedPet);
    }

    // Actualizar mascota existente
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

    // Eliminar mascota
    public void deletePet(UUID id, UUID userId) {
        Pet pet = petRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada para eliminar"));

        pet.setStatus(Status.INACTIVO);
        pet.setUpdatedIn(LocalDateTime.now());
        petRepository.save(pet);
    }

    public long countActivePets(UUID userId) {
        return petRepository.countByUserIdAndStatus(userId, Status.ACTIVO);
    }

    // metodo para manejar lógica de nivelación
    public Pet checkAndLevelUp(Pet pet) {

        Integer currentXp = pet.getPetXp();
        PetLevel currentLevel = pet.getNivel();
        PetLevel newLevel = currentLevel; // Asumimos que no sube

        if (currentXp <= 100) {
            newLevel = PetLevel.NOVATO;
        } else if (currentXp <= 350) {
            newLevel = PetLevel.EXPLORADOR;
        } else if (currentXp <= 500) {
            newLevel = PetLevel.CAZADOR;
        } else if (currentXp <= 1000) {
            newLevel = PetLevel.MAESTRO;
        } else if (currentXp <= 2000) {
            newLevel = PetLevel.ALFA;
        }

        if (newLevel != currentLevel) {
            pet.setNivel(newLevel);
            // Opcional: Agregar lógica para dar una recompensa por subir de nivel
        }

        // Retornar la mascota actualizada
        return pet;
    }

    @Transactional
    public Pet save(Pet pet) {
        return petRepository.save(pet);
    }

    // 3. Método para obtener la Entidad Pet (PetChallengeService la necesita)
    public Pet findById(UUID petId) {
        return petRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada con ID: " + petId));
    }
}