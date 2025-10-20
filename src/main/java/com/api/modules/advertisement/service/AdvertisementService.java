package com.api.modules.advertisement.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.api.common.enums.Status;
import com.api.common.enums.ZoneAd;
import com.api.modules.advertisement.dto.AdvertisementCreateDTO;
import com.api.modules.advertisement.dto.AdvertisementResponseDTO;
import com.api.modules.advertisement.dto.AdvertisementUpdateDTO;
import com.api.modules.advertisement.mapper.AdvertisementMapper;
import com.api.modules.advertisement.model.Advertisement;
import com.api.modules.advertisement.repository.AdvertisementRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j // para el log
public class AdvertisementService {
    private final AdvertisementRepository advertisementRepository;

    // obtener publicidades por zona (activas y vigentes)para el front
    public List<AdvertisementResponseDTO> getAdvertisementsByZone(ZoneAd zone) {
        return advertisementRepository.findByZoneActiveAndValid(zone, Status.ACTIVO, LocalDate.now())
                .stream()
                .map(AdvertisementMapper::toResponseDTO)
                .toList();
    }

    // para nosotros xd como admins
    // crear la publicidad
    public AdvertisementResponseDTO createAdvertisement(AdvertisementCreateDTO dto) {
        // Validar que fecha de inicio sea antes de fecha de fin
        if (dto.getStartDate().isAfter(dto.getEndDate())) {
            throw new IllegalArgumentException("La fecha de inicio debe ser anterior a la fecha de fin");
        }
        Advertisement advertisement = AdvertisementMapper.toEntity(dto);
        Advertisement savedAdvertisement = advertisementRepository.save(advertisement);

        return AdvertisementMapper.toResponseDTO(savedAdvertisement);
    }

    // todas las publicidades
    public List<AdvertisementResponseDTO> getAllAdvertisements() {
        return advertisementRepository.findAll()
                .stream()
                .map(AdvertisementMapper::toResponseDTO)
                .toList();
    }

    // publicidad por id
    public AdvertisementResponseDTO getAdvertisementById(UUID id) {
        return advertisementRepository.findById(id)
                .map(AdvertisementMapper::toResponseDTO)
                .orElseThrow(() -> new RuntimeException("Publicidad no encontrada"));
    }

    // actualizar publicidad
    public AdvertisementResponseDTO updateAdvertisement(UUID id, AdvertisementUpdateDTO dto) {
        return advertisementRepository.findById(id)
                .map(advertisement -> {
                    // Validar que las fechas si se están actualizando
                    LocalDate startDate = dto.getStartDate() != null ? dto.getStartDate()
                            : advertisement.getStartDate();
                    LocalDate endDate = dto.getEndDate() != null ? dto.getEndDate() : advertisement.getEndDate();

                    if (startDate.isAfter(endDate)) {
                        throw new IllegalArgumentException("La fecha de inicio debe ser anterior a la fecha de fin");
                    }

                    AdvertisementMapper.updateEntity(advertisement, dto);
                    Advertisement updatedAdvertisement = advertisementRepository.save(advertisement);

                    return AdvertisementMapper.toResponseDTO(updatedAdvertisement);
                })
                .orElseThrow(() -> new RuntimeException("Publicidad no encontrada para actualizar"));
    }
    

    //eliminar publicidad (opc)
    public void deleteAdvertisement(UUID id) {
        if (!advertisementRepository.existsById(id)) {
            throw new RuntimeException("Publicidad no encontrada para eliminar");
        }
        advertisementRepository.deleteById(id);
    }

    // Expirar publicidades vencidas automáticamente (cada día a las 2 AM)
    // "0 0 2 * * ?" -→ segundo (0) - minuto (0) - hora (2) - dia del mes(todos *) -
    // mes (todos *) - dia de la semana (? = cualquiera)

    // otra opción: "0 0 0 * * ?" → Cada medianoche

    @Scheduled(cron = "0 0 2 * * ?")
    @Transactional
    public void expireOldAdvertisements() {
        int count = advertisementRepository.expireOldAdvertisements(LocalDate.now());
        log.info("Publicidades expiradas: {}", count); // Útil para monitoreo
    }
}
