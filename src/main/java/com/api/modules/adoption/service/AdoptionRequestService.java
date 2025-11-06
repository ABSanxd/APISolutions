package com.api.modules.adoption.service;

import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.api.common.enums.Status;
import com.api.common.response.ApiResponse;
import com.api.modules.adoption.dto.AdoptionRequestCreateDTO;
import com.api.modules.adoption.dto.AdoptionRequestResponseDTO;
import com.api.modules.adoption.mapper.AdoptionRequestMapper;
import com.api.modules.adoption.model.AdoptionRequest;
import com.api.modules.adoption.repository.AdoptionRequestRepository;
import com.api.modules.pet.model.Pet;
import com.api.modules.pet.repository.PetRepository;
import com.api.modules.publication.model.Publication;
import com.api.modules.publication.repository.PublicationRepository;
import com.api.modules.user.model.User;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdoptionRequestService {
    
    private final AdoptionRequestRepository adoptionRequestRepository;
    private final PublicationRepository publicationRepository;
    private final PetRepository petRepository; 

    @Transactional
    public ApiResponse<AdoptionRequestResponseDTO> createRequest(User applicant, AdoptionRequestCreateDTO dto) {
        
        Publication publication = publicationRepository.findById(dto.getPublicationId())
            .orElseThrow(() -> new RuntimeException("Publicación no encontrada"));

        if (publication.getStatus() != Status.ACTIVO) {
            return ApiResponse.fail("No se puede solicitar una publicación que no está activa", 400);
        }

        if (publication.getUser().getId().equals(applicant.getId())) {
            return ApiResponse.fail("No puedes adoptar tu propia mascota", 400);
        }

        boolean alreadyPending = adoptionRequestRepository.existsByApplicantIdAndPublicationIdAndStatus(
            applicant.getId(), 
            dto.getPublicationId(), 
            Status.PENDIENTE
        );
        if (alreadyPending) {
            return ApiResponse.fail("Ya tienes una solicitud pendiente para esta mascota", 400);
        }

        int maxPetsLimit = applicant.getMaxPets();
        long currentPetCount = petRepository.countByUserIdAndStatus(applicant.getId(), Status.ACTIVO);

        if (currentPetCount >= maxPetsLimit) {
            return ApiResponse.fail("Máximo de mascotas alcanzado. No puedes adoptar más.", 400);
        }

        AdoptionRequest newRequest = new AdoptionRequest(
            applicant, 
            publication
        );
        
        AdoptionRequest savedRequest = adoptionRequestRepository.save(newRequest);

        return ApiResponse.success(
            AdoptionRequestMapper.toResponseDTO(savedRequest), 
            "Solicitud enviada correctamente"
        );
    }

    public ApiResponse<List<AdoptionRequestResponseDTO>> getReceivedRequests(User owner) {
        List<AdoptionRequest> requests = adoptionRequestRepository.findRequestsReceivedByOwnerId(owner.getId());
        List<AdoptionRequestResponseDTO> dtos = requests.stream()
            .map(AdoptionRequestMapper::toResponseDTO)
            .collect(Collectors.toList());
        return ApiResponse.success(dtos, "Solicitudes recibidas obtenidas");
    }

    public ApiResponse<List<AdoptionRequestResponseDTO>> getSentRequests(User applicant) {
        List<AdoptionRequest> requests = adoptionRequestRepository.findByApplicantId(applicant.getId());
        List<AdoptionRequestResponseDTO> dtos = requests.stream()
            .map(AdoptionRequestMapper::toResponseDTO)
            .collect(Collectors.toList());
        return ApiResponse.success(dtos, "Solicitudes enviadas obtenidas");
    }

    @Transactional
    public ApiResponse<AdoptionRequestResponseDTO> updateRequestStatus(UUID requestId, Status newStatus, User user) {
        
        AdoptionRequest request = adoptionRequestRepository.findById(requestId)
            .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        Publication publication = request.getPublication();

        switch (newStatus) {
            case ACEPTADO, RECHAZADO -> {
                if (!publication.getUser().getId().equals(user.getId())) {
                    return ApiResponse.fail("No tienes permiso para modificar esta solicitud", 403);
                }
            }
            case CANCELADO -> {
                if (!request.getApplicant().getId().equals(user.getId())) {
                    return ApiResponse.fail("No tienes permiso para cancelar esta solicitud", 403);
                }
            }
            default -> {
                return ApiResponse.fail("Acción no válida", 400);
            }
        }

        if (request.getStatus() != Status.PENDIENTE) {
            return ApiResponse.fail("Esta solicitud ya ha sido " + request.getStatus().toString().toLowerCase(), 400);
        }

        if (newStatus == Status.ACEPTADO) {
            User applicant = request.getApplicant();

            int maxPetsLimit = applicant.getMaxPets();
            long currentPetCount = petRepository.countByUserIdAndStatus(applicant.getId(), Status.ACTIVO);
            if (currentPetCount >= maxPetsLimit) {
                return ApiResponse.fail("El solicitante ha alcanzado su límite de mascotas (" + maxPetsLimit + ")", 400);
            }

            Pet newPet = new Pet();
            newPet.setUserId(applicant.getId());
            newPet.setNombre(publication.getTempName());
            newPet.setEspecie(publication.getSpecies());
            
            if (publication.getPhotos() != null && !publication.getPhotos().isEmpty()) {
                newPet.setPhoto(publication.getPhotos().get(0));
            }
            
            newPet.setPetAge(parseApproxAge(publication.getApproxAge())); 
            
            // --- ¡SOLUCIÓN AQUÍ! ---
            // Asignamos un valor por defecto ya que Publication no tiene "breed"
            newPet.setBreed("No especificado"); 
            // --- FIN DE LA SOLUCIÓN ---
            
            petRepository.save(newPet);

            publication.setStatus(Status.ADOPTADO);
            publicationRepository.save(publication);

            request.setStatus(Status.ACEPTADO);
            
            rejectOtherPendingRequests(publication.getId(), request.getId());
        } else {
            request.setStatus(newStatus);
        }

        AdoptionRequest savedRequest = adoptionRequestRepository.save(request);
        return ApiResponse.success(AdoptionRequestMapper.toResponseDTO(savedRequest), "Solicitud actualizada");
    }

    private void rejectOtherPendingRequests(UUID publicationId, UUID acceptedRequestId) {
        List<AdoptionRequest> otherRequests = 
            adoptionRequestRepository.findByPublicationIdAndStatusAndIdNot(
                publicationId, 
                Status.PENDIENTE, 
                acceptedRequestId
            );

        for (AdoptionRequest req : otherRequests) {
            req.setStatus(Status.RECHAZADO);
        }

        adoptionRequestRepository.saveAll(otherRequests);
        System.out.println("Rechazadas " + otherRequests.size() + " otras solicitudes pendientes.");
    }

    private Integer parseApproxAge(String approxAge) {
        if (approxAge == null || approxAge.isBlank()) {
            return 0;
        }
        try {
            Pattern pattern = Pattern.compile("(\\d+)"); 
            Matcher matcher = pattern.matcher(approxAge);
            if (matcher.find()) {
                int age = Integer.parseInt(matcher.group(1));
                if (approxAge.toLowerCase().contains("mes") || approxAge.toLowerCase().contains("meses")) {
                    return 0; 
                }
                return age;
            }
        } catch (NumberFormatException e) {
            // Ignorar error de parseo
        }
        return 0; 
    }
}