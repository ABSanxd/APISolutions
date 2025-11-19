package com.api.modules.publication.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.api.common.enums.Status;
import com.api.modules.publication.model.Publication;

public interface PublicationRepository extends JpaRepository<Publication, UUID> {
    List<Publication> findByStatus(Status status);
    List<Publication> findByUserId(UUID userId);

    /**
      Busca publicaciones que tengan un estado específico (ej: ACTIVO)
      y que NO pertenezcan a un usuario específico (para que no vea sus propias
      publicaciones en la lista de "disponibles" uwu).
     */
    List<Publication> findByStatusAndUser_IdNot(Status status, UUID userId);
}