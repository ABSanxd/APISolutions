package com.api.modules.advertisement.controller;

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

import com.api.common.enums.ZoneAd;
import com.api.common.response.ApiResponse;
import com.api.modules.advertisement.dto.AdvertisementCreateDTO;
import com.api.modules.advertisement.dto.AdvertisementResponseDTO;
import com.api.modules.advertisement.dto.AdvertisementUpdateDTO;
import com.api.modules.advertisement.service.AdvertisementService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/advertisements")
@RequiredArgsConstructor
public class AdvertisementController {

    private final AdvertisementService advertisementService;

    // Obtener publicidades por zona (para el frontt)
    @GetMapping("/zone/{zone}")
    public ResponseEntity<ApiResponse<List<AdvertisementResponseDTO>>> getAdvertisementsByZone(
            @PathVariable ZoneAd zone) {
        List<AdvertisementResponseDTO> advertisements = advertisementService.getAdvertisementsByZone(zone);
        return ResponseEntity.ok(ApiResponse.success(advertisements, "Publicidades obtenidas correctamente"));
    }

    // para nosotros :D
    @PostMapping
    public ResponseEntity<ApiResponse<AdvertisementResponseDTO>> createAdvertisement(
            @Valid @RequestBody AdvertisementCreateDTO dto) {
        AdvertisementResponseDTO advertisement = advertisementService.createAdvertisement(dto);
        return ResponseEntity.ok(ApiResponse.success(advertisement, "Publicidad creada correctamente"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AdvertisementResponseDTO>>> getAllAdvertisements() {
        List<AdvertisementResponseDTO> advertisements = advertisementService.getAllAdvertisements();
        return ResponseEntity.ok(ApiResponse.success(advertisements, "Todas las publicidades obtenidas"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AdvertisementResponseDTO>> getAdvertisementById(@PathVariable UUID id) {
        AdvertisementResponseDTO advertisement = advertisementService.getAdvertisementById(id);
        return ResponseEntity.ok(ApiResponse.success(advertisement, "Publicidad obtenida correctamente"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AdvertisementResponseDTO>> updateAdvertisement(
            @PathVariable UUID id,
            @RequestBody AdvertisementUpdateDTO dto) {
        AdvertisementResponseDTO advertisement = advertisementService.updateAdvertisement(id, dto);
        return ResponseEntity.ok(ApiResponse.success(advertisement, "Publicidad actualizada correctamente"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAdvertisement(@PathVariable UUID id) {
        advertisementService.deleteAdvertisement(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Publicidad eliminada correctamente"));
    }

}
