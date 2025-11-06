package com.api.modules.adoption.mapper;

import com.api.modules.adoption.dto.AdoptionRequestResponseDTO;
import com.api.modules.adoption.model.AdoptionRequest;
import com.api.modules.publication.model.Publication;
import com.api.modules.user.model.User;

public class AdoptionRequestMapper {

    public static AdoptionRequestResponseDTO toResponseDTO(AdoptionRequest request) {
        if (request == null) {
            return null;
        }

        User applicant = request.getApplicant();
        Publication publication = request.getPublication();
        User owner = (publication != null) ? publication.getUser() : null;

        AdoptionRequestResponseDTO dto = new AdoptionRequestResponseDTO();
        dto.setId(request.getId());
        dto.setStatus(request.getStatus()); 
        dto.setCreatedAt(request.getCreatedAt());
        
        // (Línea de 'message' eliminada)

        // Info del Solicitante
        if (applicant != null) {
            AdoptionRequestResponseDTO.ApplicantInfoDTO applicantDTO = new AdoptionRequestResponseDTO.ApplicantInfoDTO();
            applicantDTO.setUserId(applicant.getId());
            applicantDTO.setName(applicant.getName());
            applicantDTO.setDistrict(applicant.getDistrict());
            applicantDTO.setProvince(applicant.getProvince());
            dto.setApplicant(applicantDTO);
        }

        // Info de la Publicación (y su dueño)
        if (publication != null) {
            AdoptionRequestResponseDTO.PublicationInfoDTO publicationDTO = new AdoptionRequestResponseDTO.PublicationInfoDTO();
            publicationDTO.setPublicationId(publication.getId());
            publicationDTO.setPetName(publication.getTempName());
            
            // --- CAMBIO AQUÍ ---
            // Usamos la primera foto de la lista como portada
            if (publication.getPhotos() != null && !publication.getPhotos().isEmpty()) {
                publicationDTO.setPetPhoto(publication.getPhotos().get(0));
            }
            // --- FIN DEL CAMBIO ---

            if (owner != null) {
                publicationDTO.setOwnerId(owner.getId());
                publicationDTO.setOwnerName(owner.getName());
            }
            dto.setPublication(publicationDTO);
        }

        return dto;
    }
}