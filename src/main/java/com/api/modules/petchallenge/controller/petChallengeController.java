package com.api.modules.petchallenge.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.modules.petchallenge.dto.PetChallengeCreateDTO;
import com.api.modules.petchallenge.models.PetChallenge;
import com.api.modules.petchallenge.service.PetChallengeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/pets/{petId}/challenges")
@RequiredArgsConstructor
public class petChallengeController {
    private final PetChallengeService petChallengeService;

    // POST /api/pets/{petId}/challenges
    // Marcar reto como completado
    @PostMapping
    public ResponseEntity<PetChallenge> completeChallenge(
        @PathVariable UUID petId,
        @Valid @RequestBody PetChallengeCreateDTO dto
    ) {
        PetChallenge newRecord = petChallengeService.completeChallenge(petId, dto);
        return new ResponseEntity<>(newRecord, HttpStatus.CREATED);
    }

}
