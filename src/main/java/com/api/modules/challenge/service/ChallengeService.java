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


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

//este service contiene la logica central de gamificacion (reto completado, actualizacion de XP, verificacion de logros, etc)
@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
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

    // ---------------------------------
    //// Nuevo método para obtener Challenge pero en su formato Entidad (necesario para PetChallengeService)
    public Challenge findChallengeEntityById(UUID id) {
        return challengeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reto no encontrado con ID: " + id));
    }

}
