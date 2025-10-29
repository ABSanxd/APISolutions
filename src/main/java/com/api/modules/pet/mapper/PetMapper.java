package com.api.modules.pet.mapper;

import java.util.UUID;

import com.api.modules.pet.dto.PetCreateDTO;
import com.api.modules.pet.dto.PetResponseDTO;
import com.api.modules.pet.dto.PetUpdateDTO;
import com.api.modules.pet.model.Pet;

public class PetMapper {

    public static Pet toEntity(PetCreateDTO dto, UUID userId) {
        Pet pet = new Pet();
        pet.setUserId(userId);
        pet.setNombre(dto.getNombre());
        pet.setEspecie(dto.getEspecie());
        pet.setBreed(dto.getBreed());
        pet.setPetAge(dto.getPetAge());
        pet.setPetWeight(dto.getPetWeight());
        pet.setPhoto(dto.getPhoto());
        return pet;
    }

    public static void updateEntity(Pet pet, PetUpdateDTO dto) {
        if (dto.getNombre() != null)
            pet.setNombre(dto.getNombre());
        if (dto.getEspecie() != null)
            pet.setEspecie(dto.getEspecie());
        if (dto.getBreed() != null)
            pet.setBreed(dto.getBreed());
        if (dto.getPetAge() != null)
            pet.setPetAge(dto.getPetAge());
        if (dto.getPetWeight() != null)
            pet.setPetWeight(dto.getPetWeight());
        if (dto.getPhoto() != null)
            pet.setPhoto(dto.getPhoto());
    }

    public static PetResponseDTO toResponseDTO(Pet pet) {
        PetResponseDTO dto = new PetResponseDTO();
        dto.setId(pet.getId());
        dto.setUserId(pet.getUserId());
        dto.setNombre(pet.getNombre());
        dto.setEspecie(pet.getEspecie());
        dto.setNivel(pet.getNivel());
        dto.setPetXp(pet.getPetXp());
        dto.setBreed(pet.getBreed());
        dto.setPetAge(pet.getPetAge());
        dto.setPetWeight(pet.getPetWeight());
        dto.setPhoto(pet.getPhoto());
        dto.setStatus(pet.getStatus());
        dto.setCreatedAt(pet.getCreatedAt());
        dto.setUpdatedIn(pet.getUpdatedIn());
        return dto;
    }
}