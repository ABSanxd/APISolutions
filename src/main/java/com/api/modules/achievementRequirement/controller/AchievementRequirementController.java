package com.api.modules.achievementRequirement.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.common.response.ApiResponse;
import com.api.modules.achievementRequirement.dto.AchievementRequirementCreateDTO;
import com.api.modules.achievementRequirement.dto.AchievementRequirementResponseDTO;
import com.api.modules.achievementRequirement.dto.AchievementRequirementUpdateDTO;
import com.api.modules.achievementRequirement.service.AchievementRequirementService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/achievement-requirements")
@RequiredArgsConstructor
public class AchievementRequirementController {

    private final AchievementRequirementService requirementService;

    @PostMapping
    public ResponseEntity<ApiResponse<AchievementRequirementResponseDTO>> create(
            @Valid @RequestBody AchievementRequirementCreateDTO dto) {
        AchievementRequirementResponseDTO created = requirementService.create(dto);

        return ResponseEntity.ok(ApiResponse.success(created, "Requisito creado con éxito"));
    }

    // Obtener todos los requisitos de un logro específico
    @GetMapping("/achievement/{achievementId}")
    public ResponseEntity<ApiResponse<List<AchievementRequirementResponseDTO>>> getByAchievement(
            @PathVariable UUID achievementId) {
        List<AchievementRequirementResponseDTO> requirements = requirementService.getByAchievementId(achievementId);
        return ResponseEntity.ok(ApiResponse.success(requirements,
                requirements.size() + " Requisitos obtenidos correctamente para el logro"));
    }

    // Obtener un requisito por ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AchievementRequirementResponseDTO>> getById(@PathVariable UUID id) {
        AchievementRequirementResponseDTO requirement = requirementService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(requirement, "Requisito obtenido correctamente"));
    }

    // obtener todos los requisitos
    @GetMapping
    public ResponseEntity<ApiResponse<List<AchievementRequirementResponseDTO>>> getAll() {
        List<AchievementRequirementResponseDTO> requirements = requirementService.getAll();
        return ResponseEntity.ok(ApiResponse.success(requirements, "Requisitos obtenidos correctamente"));
    }

    // actualizar un requisito
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AchievementRequirementResponseDTO>> update(
            @PathVariable UUID id,
            @Valid @RequestBody AchievementRequirementUpdateDTO dto) {
        AchievementRequirementResponseDTO updated = requirementService.update(id, dto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Requisito actualizado correctamente"));
    }

    // eliminar un requisito
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        requirementService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Requisito eliminado correctamente"));
    }
}