package com.api.modules.vaccine.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping; // <-- IMPORTANTE: Importar PUT
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.common.response.ApiResponse;
import com.api.modules.user.model.User;
import com.api.modules.vaccine.dto.VaccineCreateDTO;
import com.api.modules.vaccine.dto.VaccineDoseDTO;
import com.api.modules.vaccine.dto.VaccineResponseDTO;
import com.api.modules.vaccine.dto.VaccineUpdateDTO; // <-- IMPORTANTE: Importar el DTO de actualización
import com.api.modules.vaccine.service.VaccineService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1") // Ruta base
public class VaccineController {

    private final VaccineService vaccineService;

    // --- ENDPOINTS DE VACUNAS (anidados en mascotas) ---

    @GetMapping("/pets/{petId}/vaccines")
    public ResponseEntity<ApiResponse<List<VaccineResponseDTO>>> getVaccinesByPet(
            @PathVariable UUID petId,
            @AuthenticationPrincipal User user) {
        // (En un futuro, deberías validar que el petId pertenece al user.getId())
        // Por ahora, tu servicio ya valida la pertenencia en create, update y delete.
        List<VaccineResponseDTO> vaccines = vaccineService.getVaccinesByPet(petId);
        return ResponseEntity.ok(ApiResponse.success(vaccines, "Vacunas obtenidas"));
    }

    @PostMapping("/pets/{petId}/vaccines")
    public ResponseEntity<ApiResponse<VaccineResponseDTO>> createVaccine(
            @PathVariable UUID petId,
            @Valid @RequestBody VaccineCreateDTO dto,
            @AuthenticationPrincipal User user) {
        VaccineResponseDTO newVaccine = vaccineService.createVaccine(dto, petId, user.getId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(newVaccine, "Vacuna registrada"));
    }

    // --- ¡NUEVO ENDPOINT DE ACTUALIZACIÓN! ---
    @PutMapping("/pets/{petId}/vaccines/{vaccineId}")
    public ResponseEntity<ApiResponse<VaccineResponseDTO>> updateVaccine(
            @PathVariable UUID petId,
            @PathVariable UUID vaccineId,
            @Valid @RequestBody VaccineUpdateDTO dto,
            @AuthenticationPrincipal User user) {
        
        VaccineResponseDTO updatedVaccine = vaccineService.updateVaccine(vaccineId, dto, petId, user.getId());
        return ResponseEntity.ok(ApiResponse.success(updatedVaccine, "Vacuna actualizada"));
    }
    
    @DeleteMapping("/pets/{petId}/vaccines/{vaccineId}")
    public ResponseEntity<ApiResponse<Void>> deleteVaccine(
            @PathVariable UUID petId,
            @PathVariable UUID vaccineId,
            @AuthenticationPrincipal User user) {
        vaccineService.deleteVaccine(vaccineId, petId, user.getId());
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(ApiResponse.success(null, "Vacuna eliminada"));
    }

    // --- ENDPOINTS DE DOSIS (anidados en vacunas) ---

    @PostMapping("/vaccines/{vaccineId}/doses")
    public ResponseEntity<ApiResponse<VaccineDoseDTO>> addDoseToVaccine(
            @PathVariable UUID vaccineId,
            @Valid @RequestBody VaccineDoseDTO dto,
            @AuthenticationPrincipal User user) {
        // (Aquí también faltaría validar que la vacuna pertenece al usuario)
        VaccineDoseDTO newDose = vaccineService.addDoseToVaccine(dto, vaccineId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(newDose, "Dosis añadida"));
    }
    
    @DeleteMapping("/doses/{doseId}")
    public ResponseEntity<ApiResponse<Void>> deleteDose(
            @PathVariable UUID doseId,
            @AuthenticationPrincipal User user) {
        // (Validar que la dosis pertenece al usuario)
        vaccineService.deleteDose(doseId);
         return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(ApiResponse.success(null, "Dosis eliminada"));
    }

    @PutMapping("/doses/{doseId}/status")
    public ResponseEntity<ApiResponse<VaccineDoseDTO>> updateDoseStatus(
            @PathVariable UUID doseId,
            @RequestBody Map<String, Boolean> body,
            @AuthenticationPrincipal User user) {
        // (Validar que la dosis pertenece al usuario)
        Boolean applied = body.get("applied");
        if (applied == null) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.fail("El campo 'applied' (boolean) es requerido", 400));
        }
        VaccineDoseDTO updatedDose = vaccineService.updateDoseStatus(doseId, applied);
        return ResponseEntity.ok(ApiResponse.success(updatedDose, "Estado de dosis actualizado"));
    }
}