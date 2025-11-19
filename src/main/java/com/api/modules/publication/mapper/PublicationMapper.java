package com.api.modules.publication.mapper;

import java.util.UUID; // <-- AÑADIR IMPORT

import com.api.modules.publication.dto.PublicationCreateDTO;
import com.api.modules.publication.dto.PublicationResponseDTO;
import com.api.modules.publication.dto.PublicationUpdateDTO;
import com.api.modules.publication.model.Publication;
import com.api.modules.user.model.User;

public class PublicationMapper {

    public static Publication toEntity(PublicationCreateDTO dto, User user) {
        Publication p = new Publication();
        p.setTempName(dto.getTempName());
        p.setSpecies(dto.getSpecies());
        p.setApproxAge(dto.getApproxAge());
        p.setPhotos(dto.getPhotos());
        p.setDescription(dto.getDescription());
        p.setContact(dto.getContact());
        p.setUser(user); 
        p.setDepartment(dto.getDepartment());
        p.setProvince(dto.getProvince());
        p.setDistrict(dto.getDistrict());
        p.setShared(0);
        return p;
    }

    public static void updateEntity(Publication p, PublicationUpdateDTO dto) {
        if (dto.getTempName() != null)
            p.setTempName(dto.getTempName());
        if (dto.getSpecies() != null)
            p.setSpecies(dto.getSpecies());
        if (dto.getApproxAge() != null)
            p.setApproxAge(dto.getApproxAge());
        
        if (dto.getPhotos() != null && !dto.getPhotos().isEmpty())
            p.setPhotos(dto.getPhotos());

        if (dto.getDescription() != null)
            p.setDescription(dto.getDescription());
        if (dto.getContact() != null)
            p.setContact(dto.getContact());
        if (dto.getStatus() != null)
            p.setStatus(dto.getStatus());
        if (dto.getDepartment() != null)
            p.setDepartment(dto.getDepartment());
        if (dto.getProvince() != null)
            p.setProvince(dto.getProvince());
        if (dto.getDistrict() != null)
            p.setDistrict(dto.getDistrict());
    }

    public static PublicationResponseDTO toResponseDTO(Publication p) {
        PublicationResponseDTO dto = new PublicationResponseDTO();
        dto.setId(p.getId());
        dto.setTempName(p.getTempName());
        dto.setSpecies(p.getSpecies());
        dto.setApproxAge(p.getApproxAge());
        
        dto.setPhotos(p.getPhotos());
        
        dto.setDescription(p.getDescription());
        dto.setContact(p.getContact());
        dto.setStatus(p.getStatus());
        dto.setCreationDate(p.getCreationDate());
        dto.setUpdateDate(p.getUpdateDate());
        
        if (p.getUser() != null) {
            PublicationResponseDTO.UserDTO userDTO = new PublicationResponseDTO.UserDTO();
            userDTO.setId(p.getUser().getId());
            userDTO.setName(p.getUser().getName());
            dto.setUser(userDTO);
        }
            
        dto.setDepartment(p.getDepartment());
        dto.setProvince(p.getProvince());
        dto.setDistrict(p.getDistrict());
        dto.setShared(p.getShared());
        
        // Calculamos los likes dinámicamente
        dto.setLikes(p.getLikedBy() != null ? p.getLikedBy().size() : 0);
        dto.setLikedByMe(false);
        
        return dto;
    }

    
     //Sobrecarga de 'toResponseDTO' que sabe quién está mirando, para calcular el campo 'likedByMe'.
     
    public static PublicationResponseDTO toResponseDTO(Publication p, UUID currentUserId) {
        PublicationResponseDTO dto = toResponseDTO(p); // Llama al método base

        if (p.getLikedBy() != null && currentUserId != null) {
            // Revisa si el Set de 'likedBy' contiene al usuario actual
            dto.setLikedByMe(
                p.getLikedBy().stream().anyMatch(user -> user.getId().equals(currentUserId))
            );
        }
        return dto;
    }
}