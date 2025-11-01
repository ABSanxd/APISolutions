package com.api.modules.challenge.service;

import com.api.common.enums.Category;
import com.api.common.enums.Frequency;
import com.api.common.exception.ResourceNotFoundException;
import com.api.modules.challenge.dto.ChallengeDTO;
import com.api.modules.challenge.mapper.ChallengeMapper;
import com.api.modules.challenge.model.Challenge;
import com.api.modules.challenge.repository.ChallengeRepository;
import com.api.modules.challengerequirements.model.ChallengeRequirements;
import com.api.modules.challengerequirements.repository.ChallengeRequirementsRepository;
import com.api.modules.petchallenge.models.PetChallenge;
import com.api.modules.petchallenge.repository.PetChallengeRepository;
import com.api.modules.pet.model.Pet;
import com.api.modules.pet.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;



//este service contiene la logica central de gamificacion (reto completado, actualizacion de XP, verificacion de logros, etc)
@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final PetRepository petRepository;
    private final PetChallengeRepository petChallengeRepository;
    private final ChallengeRequirementsRepository requirementsRepository;
    private final ChallengeMapper challengeMapper;

    

    public ChallengeDTO getChallengeById(UUID id) {
    Challenge challenge = challengeRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Challenge not found with id: " + id));
    
    return challengeMapper.toDTO(challenge);
}

    // método de consulta (GET /api/v1/challenges)
    public List<ChallengeDTO> findAllChallenges(Category category, Frequency frequency) {
        List<Challenge> challenges; 
        if(category != null && frequency != null) {
            challenges = challengeRepository.findByCategoryAndFrequency(category, frequency);
        } else if (category != null) {
            challenges = challengeRepository.findByCategory(category);
        } else if (frequency != null) {
            challenges = challengeRepository.findByFrequency(frequency);
        } else {
            challenges = challengeRepository.findAll();
        }
        return challenges.stream().map(challengeMapper::toDTO).collect(Collectors.toList());
        
    }

    
    //logica para marcar reto como completado, actualizar xp y verificar logros
    //endpoint: POST /api/v1/challenges/pets/{petId}/complete/{challengeId}
    @Transactional
    public void completeChallenge(UUID petId, UUID challengeId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found with id: " + petId));

        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new ResourceNotFoundException("Challenge not found with id: " + challengeId));
                //si no encuentra la mascota o el reto, lanza excepcion
    
                

        //registrar avance
        PetChallenge petChallenge = new PetChallenge();
        petChallenge.setPet(pet);
        petChallenge.setChallenge(challenge);
        petChallengeRepository.save(petChallenge);

        //actualizar puntos/xp 
        pet.setPetXp(pet.getPetXp() + challenge.getPoints());
        petRepository.save(pet);


        //Verificar logros despues de la actualizacion
        checkAndAwardAchievements(petId, challengeId);
    }

    private void checkAndAwardAchievements(UUID petId, UUID completedChallengeId) {
        
        // Obtener todos los requisitos que usan este reto.
        List<ChallengeRequirements> requirements = requirementsRepository.findByChallengeId(completedChallengeId);

        //Agrupar por el ID de Logro al que pertenecen.
        requirements.stream()
            .collect(Collectors.groupingBy(ChallengeRequirements::getAchievementId))
            .forEach((achievementId, requirementList) -> {
                
                // Para este Logro "achievementId", verifica si todos sus requisitos se cumplen.
                boolean isAchievementComplete = requirementList.stream().allMatch(req -> {
                    
                    // Contar cuántas veces la mascota ha completado el reto requerido.
                    long count = petChallengeRepository.countByPetIdAndChallengeId(
                            petId, req.getChallenge().getId()); //método countByPetIdAndChallengeId

                    // Compara si el conteo actual >= repeticiones requeridas.
                    return count >= req.getRepetitions();
                });

                if (isAchievementComplete) {
                    // Lógica para otorgar el logro a la mascota.
                    System.out.println("LOGRO DESBLOQUEADO (ID temporal): " + achievementId + " para la mascota: " + petId);
                }
            });
    }


    





}
