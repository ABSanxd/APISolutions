package com.api.modules.publication.mapper;

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
        p.setPhoto(dto.getPhoto());
        p.setDescription(dto.getDescription());
        p.setContact(dto.getContact());
        p.setUser(user);
        if (dto.getSpecies() != null && dto.getSpecies().name().equals("OTRO")) {
            p.setDescription((dto.getDescription() == null ? "" : dto.getDescription()) + "\nOTROS:" + dto.getOtros());
        }
        return p;
    }

    public static void updateEntity(Publication p, PublicationUpdateDTO dto) {
        if (dto.getTempName() != null)
            p.setTempName(dto.getTempName());
        if (dto.getSpecies() != null)
            p.setSpecies(dto.getSpecies());
        if (dto.getApproxAge() != null)
            p.setApproxAge(dto.getApproxAge());
        if (dto.getPhoto() != null)
            p.setPhoto(dto.getPhoto());
        if (dto.getDescription() != null)
            p.setDescription(dto.getDescription());
        if (dto.getContact() != null)
            p.setContact(dto.getContact());
        if (dto.getStatus() != null)
            p.setStatus(dto.getStatus());
        if (dto.getOtros() != null && p.getSpecies() != null && p.getSpecies().name().equals("OTRO"))
            p.setDescription((p.getDescription() == null ? "" : p.getDescription()) + "\nOTROS:" + dto.getOtros());
    }

    public static PublicationResponseDTO toResponseDTO(Publication p) {
        PublicationResponseDTO dto = new PublicationResponseDTO();
        dto.setId(p.getId());
        dto.setTempName(p.getTempName());
        dto.setSpecies(p.getSpecies());
        dto.setApproxAge(p.getApproxAge());
        dto.setPhoto(p.getPhoto());
        dto.setDescription(p.getDescription());
    dto.setContact(p.getContact());
        dto.setStatus(p.getStatus());
        dto.setCreationDate(p.getCreationDate());
        dto.setUpdateDate(p.getUpdateDate());
        if (p.getUser() != null)
            dto.setUserId(p.getUser().getId());
        return dto;
    }
}
