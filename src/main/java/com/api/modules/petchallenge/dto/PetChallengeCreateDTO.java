package com.api.modules.petchallenge.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record PetChallengeCreateDTO(

        @NotNull(message = "El ID del reto es obligatorio.") UUID challengeId) {
}
