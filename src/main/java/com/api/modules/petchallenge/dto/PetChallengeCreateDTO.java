package com.api.modules.petchallenge.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record PetChallengeCreateDTO(
    // ID del reto completado (viene del catálogo Challenge)
    @NotNull(message = "El ID del reto es obligatorio.")
    UUID challengeId
) {}
