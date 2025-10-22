package com.api.modules.publication.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.common.enums.Status;
import com.api.modules.publication.model.Publication;

public interface PublicationRepository extends JpaRepository<Publication, Long> {
    List<Publication> findByStatus(Status status);
    List<Publication> findByUserId(java.util.UUID userId);
}
