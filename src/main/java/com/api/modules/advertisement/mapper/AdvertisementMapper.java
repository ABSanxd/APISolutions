package com.api.modules.advertisement.mapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.api.modules.advertisement.dto.AdvertisementCreateDTO;
import com.api.modules.advertisement.dto.AdvertisementResponseDTO;
import com.api.modules.advertisement.dto.AdvertisementUpdateDTO;
import com.api.modules.advertisement.model.Advertisement;
import com.api.modules.advertisement.model.ContactDetails;
import com.api.modules.advertisement.model.SocialMedia;

public class AdvertisementMapper {

    public static AdvertisementResponseDTO toResponseDTO(Advertisement advertisement) {
        if (advertisement == null) {
            return null;
        }
        AdvertisementResponseDTO dto = new AdvertisementResponseDTO();
        dto.setId(advertisement.getId());
        dto.setName(advertisement.getName());
        dto.setDescription(advertisement.getDescription());
        dto.setLink(advertisement.getLink());
        dto.setImageUrl(advertisement.getImageUrl());
        dto.setZone(advertisement.getZone());
        dto.setPriority(advertisement.getPriority());
        dto.setContact(advertisement.getContact()); // Se copia el objeto completo
        dto.setStartDate(advertisement.getStartDate());
        dto.setEndDate(advertisement.getEndDate());
        dto.setStatus(advertisement.getStatus());
        return dto;
    }

    public static List<AdvertisementResponseDTO> toResponseDTOList(List<Advertisement> advertisements) {
        if (advertisements == null || advertisements.isEmpty()) {
            return new ArrayList<>();
        }
        return advertisements.stream()
                .map(AdvertisementMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // crear
    public static Advertisement toEntity(AdvertisementCreateDTO dto) {
        if (dto == null) {
            return null;
        }
        Advertisement advertisement = new Advertisement();
        advertisement.setName(dto.getName());
        advertisement.setDescription(dto.getDescription());
        advertisement.setLink(dto.getLink());
        advertisement.setImageUrl(dto.getImageUrl());
        advertisement.setZone(dto.getZone());
        advertisement.setPriority(dto.getPriority());
        advertisement.setStartDate(dto.getStartDate());
        advertisement.setEndDate(dto.getEndDate());
        advertisement.setIsRenewable(dto.getIsRenewable());
        advertisement.setContact(dto.getContact());
        advertisement.setStatus(dto.getStatus());
        return advertisement;
    }

    // para actualizar
    public static void updateEntity(Advertisement advertisement, AdvertisementUpdateDTO dto) {
        if (dto.getName() != null)
            advertisement.setName(dto.getName());
        if (dto.getDescription() != null)
            advertisement.setDescription(dto.getDescription());
        if (dto.getLink() != null)
            advertisement.setLink(dto.getLink());
        if (dto.getImageUrl() != null)
            advertisement.setImageUrl(dto.getImageUrl());
        if (dto.getZone() != null)
            advertisement.setZone(dto.getZone());
        if (dto.getPriority() != null)
            advertisement.setPriority(dto.getPriority());
        if (dto.getStartDate() != null)
            advertisement.setStartDate(dto.getStartDate());
        if (dto.getEndDate() != null)
            advertisement.setEndDate(dto.getEndDate());
        if (dto.getIsRenewable() != null)
            advertisement.setIsRenewable(dto.getIsRenewable());

        if (dto.getStatus() != null)
            advertisement.setStatus(dto.getStatus());
        // para contact details
        if (dto.getContact() != null) {
            ContactDetails entityContact = advertisement.getContact();
            if (entityContact == null) {
                entityContact = new ContactDetails();
                advertisement.setContact(entityContact); // Establecer el nuevo objeto en la entidad
            }

            // Actualizar campo por campo
            ContactDetails dtoContact = dto.getContact();

            if (dtoContact.getEmail() != null) {
                entityContact.setEmail(dtoContact.getEmail());
            }
            if (dtoContact.getPhone() != null) {
                entityContact.setPhone(dtoContact.getPhone());
            }
            if (dtoContact.getAddress() != null) {
                entityContact.setAddress(dtoContact.getAddress());
            }

            // Manejar SocialMedia de ContactDetails
            if (dtoContact.getSocialMedia() != null) {
                SocialMedia entitySocialMedia = entityContact.getSocialMedia();
                if (entitySocialMedia == null) {
                    entitySocialMedia = new SocialMedia();
                    entityContact.setSocialMedia(entitySocialMedia);
                }

                SocialMedia dtoSocialMedia = dtoContact.getSocialMedia();

                // Actualizar SocialMedia campo por campo
                if (dtoSocialMedia.getType() != null) {
                    entitySocialMedia.setType(dtoSocialMedia.getType());
                }
                if (dtoSocialMedia.getUrl() != null) {
                    entitySocialMedia.setUrl(dtoSocialMedia.getUrl());
                }

            } else if (dto.getContact().getSocialMedia() == null
                    && advertisement.getContact() != null
                    && advertisement.getContact().getSocialMedia() != null) {

            }

        }

        advertisement.setUpdatedAt(LocalDateTime.now());
    }

}
