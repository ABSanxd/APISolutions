package com.api.modules.publication.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.common.enums.Status;
import com.api.modules.publication.model.Publication;

public interface PublicationRepository extends JpaRepository<Publication, UUID> {
    List<Publication> findByStatus(Status status);
    List<Publication> findByUserId(UUID userId);
}