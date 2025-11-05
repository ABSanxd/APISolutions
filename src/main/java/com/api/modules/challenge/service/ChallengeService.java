package com.api.modules.challenge.service;

import com.api.common.enums.Category;
import com.api.common.enums.Frequency;
import com.api.common.enums.Status;
import com.api.common.exception.ResourceNotFoundException;
import com.api.modules.challenge.dto.ChallengeCreateDTO;
import com.api.modules.challenge.dto.ChallengeResponseDTO;
import com.api.modules.challenge.dto.ChallengeUpdateDTO;
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

    public ChallengeResponseDTO createChallenge(ChallengeCreateDTO dto) {

        // Mapear el DTO a la entidad
        Challenge challengeToSave = challengeMapper.toEntity(dto);

        // Guardar en la base de datos (ChallengeRepository heredado de JpaRepository)
        Challenge savedChallenge = challengeRepository.save(challengeToSave);

        // Devolver la respuesta mapeada (reutilizando ChallengeResponseDTO)
        return challengeMapper.toResponseDTO(savedChallenge);
    }

    public ChallengeResponseDTO getChallengeById(UUID id) {
        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reto no encontrado con ID: " + id));

        return challengeMapper.toResponseDTO(challenge);
    }

    public ChallengeResponseDTO updateChallenge(UUID id, ChallengeUpdateDTO dto) {

        Challenge existingChallenge = challengeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reto no encontrado con ID: " + id));

        ChallengeMapper.updateEntity(existingChallenge, dto);

        Challenge updatedChallenge = challengeRepository.save(existingChallenge);
        return challengeMapper.toResponseDTO(updatedChallenge);
    }

    public ChallengeResponseDTO deletechallenge(UUID id) {

        Challenge existingChallenge = challengeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reto no encontrado con ID: " + id));

        existingChallenge.setStatus(Status.INACTIVO);

        Challenge inactiveChallenge = challengeRepository.save(existingChallenge);
        return challengeMapper.toResponseDTO(inactiveChallenge);
    }

    public ChallengeResponseDTO activateChallenge(UUID id) {
        Challenge existingChallenge = challengeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reto no encontrado con ID: " + id));

        if (existingChallenge.getStatus() == Status.ACTIVO) {
            throw new IllegalStateException("El reto ya está activo.");
        }

        existingChallenge.setStatus(Status.ACTIVO);
        Challenge activated = challengeRepository.save(existingChallenge);

        return challengeMapper.toResponseDTO(activated);
    }  

    // método de consulta (GET /api/v1/challenges)
    public List<ChallengeResponseDTO> findAllChallenges(Category category, Frequency frequency) {
        List<Challenge> challenges;
        if (category != null && frequency != null) {
            challenges = challengeRepository.findByCategoryAndFrequency(category, frequency);
        } else if (category != null) {
            challenges = challengeRepository.findByCategory(category);
        } else if (frequency != null) {
            challenges = challengeRepository.findByFrequency(frequency);
        } else {
            challenges = challengeRepository.findAll();
        }
        return challenges.stream().map(challengeMapper::toResponseDTO).collect(Collectors.toList());

    }

    // logica para marcar reto como completado, actualizar xp y verificar logros
    // endpoint: POST /api/v1/challenges/pets/{petId}/complete/{challengeId}
    @Transactional
    public void completeChallenge(UUID petId, UUID challengeId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada con ID: " + petId));

        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new ResourceNotFoundException("Reto no encontrado con ID: " + challengeId));
        // si no encuentra la mascota o el reto, lanza excepcion

        // registrar avance
        PetChallenge petChallenge = new PetChallenge();
        petChallenge.setPet(pet);
        petChallenge.setChallenge(challenge);
        petChallengeRepository.save(petChallenge);

        // actualizar puntos/xp
        pet.setPetXp(pet.getPetXp() + challenge.getPoints());
        petRepository.save(pet);

        // Verificar logros despues de la actualizacion
        checkAndAwardAchievements(petId, challengeId);
    }

    private void checkAndAwardAchievements(UUID petId, UUID completedChallengeId) {

        // Obtener todos los requisitos que usan este reto.
        List<ChallengeRequirements> requirements = requirementsRepository.findByChallengeId(completedChallengeId);

        // Agrupar por el ID de Logro al que pertenecen.
        requirements.stream()
                .collect(Collectors.groupingBy(ChallengeRequirements::getAchievementId))
                .forEach((achievementId, requirementList) -> {

                    // Para este Logro "achievementId", verifica si todos sus requisitos se cumplen.
                    boolean isAchievementComplete = requirementList.stream().allMatch(req -> {

                        // Contar cuántas veces la mascota ha completado el reto requerido.
                        long count = petChallengeRepository.countByPetIdAndChallengeId(
                                petId, req.getChallenge().getId()); // método countByPetIdAndChallengeId

                        // Compara si el conteo actual >= repeticiones requeridas.
                        return count >= req.getRepetitions();
                    });

                    if (isAchievementComplete) {
                        // Lógica para otorgar el logro a la mascota.
                        System.out.println(
                                "LOGRO DESBLOQUEADO (ID temporal): " + achievementId + " para la mascota: " + petId);
                    }
                });
    }

}
