package com.api.modules.petchallenge.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.api.modules.challenge.model.Challenge;
import com.api.modules.challenge.service.ChallengeService;
import com.api.modules.pet.model.Pet;
import com.api.modules.pet.service.PetService;
import com.api.modules.petchallenge.dto.PetChallengeCreateDTO;
import com.api.modules.petchallenge.models.PetChallenge;
import com.api.modules.petchallenge.repository.PetChallengeRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PetChallengeService {
    private final PetChallengeRepository petChallengeRepository;
    private final PetService petService; // Ahora con findById, save, checkAndLevelUp
    private final ChallengeService challengeService; // Usado para obtener el Reto
    // private final AchievementService achievementService; // Hook para la FASE 3

    // Marcar reto como completado
    @Transactional
    public PetChallenge completeChallenge(UUID petId, PetChallengeCreateDTO dto) {
        
        Pet pet = petService.findById(petId);
        
        // CORRECCIÓN: Acceder al campo directamente (dto.challengeId)
        Challenge challenge = challengeService.findChallengeEntityById(dto.challengeId()); 

        // 1. Crear RetoMascota (Registro)
        PetChallenge petChallenge = new PetChallenge();
        petChallenge.setPet(pet);
        petChallenge.setChallenge(challenge);
        petChallengeRepository.save(petChallenge);

        // 2. Sumar XP
        int xpGanado = challenge.getPoints(); // Asumiendo que Challenge tiene getPoints()
        pet.setPetXp(pet.getPetXp() + xpGanado);

        // 3. Verificar Nivel y Guardar
        petService.checkAndLevelUp(pet); 
        petService.save(pet); 

        // 4. ⭐ DISPARAR EL HOOK DE LOGROS (Fase 3)
        /*achievementService.checkAchievements(pet, challenge); */
        
        return petChallenge;
    }

}
