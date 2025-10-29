package com.api.modules.pet.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal; 
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.common.response.ApiResponse;
import com.api.modules.pet.dto.PetCreateDTO;
import com.api.modules.pet.dto.PetResponseDTO;
import com.api.modules.pet.dto.PetUpdateDTO;
import com.api.modules.pet.service.PetService;
import com.api.modules.user.model.User; 

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pets")
public class PetController {
    
    private final PetService petService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PetResponseDTO>>> getAllPets(
            @AuthenticationPrincipal User user) { 
        List<PetResponseDTO> pets = petService.getAllPetsByUser(user.getId()); 
        return ResponseEntity.ok(ApiResponse.success(pets, "Mascotas obtenidas correctamente"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PetResponseDTO>> getPetById(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user) { 
        PetResponseDTO pet = petService.getPetById(id, user.getId()); 
        return ResponseEntity.ok(ApiResponse.success(pet, "Mascota encontrada"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PetResponseDTO>> createPet(
            @Valid @RequestBody PetCreateDTO petDto,
            @AuthenticationPrincipal User user) { 
        PetResponseDTO createdPet = petService.createPet(petDto, user.getId()); 
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(createdPet, "Mascota creada exitosamente"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PetResponseDTO>> updatePet(
            @PathVariable UUID id,
            @Valid @RequestBody PetUpdateDTO petDto,
            @AuthenticationPrincipal User user) { 
        PetResponseDTO updatedPet = petService.updatePet(id, petDto, user.getId()); 
        return ResponseEntity.ok(ApiResponse.success(updatedPet, "Usuario actualizado correctamente"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePet(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user) { 
        petService.deletePet(id, user.getId()); 
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(ApiResponse.success(null, "Mascota eliminada correctamente"));
    }

    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> countActivePets(
            @AuthenticationPrincipal User user) { 
        long count = petService.countActivePets(user.getId()); 
        return ResponseEntity.ok(ApiResponse.success(count, "Conteo de mascotas obtenido"));
    }
}