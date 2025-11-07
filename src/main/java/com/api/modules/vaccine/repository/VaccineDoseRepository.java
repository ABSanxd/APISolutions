package com.api.modules.vaccine.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.api.modules.vaccine.model.VaccineDose;

@Repository
public interface VaccineDoseRepository extends JpaRepository<VaccineDose, UUID> {
    
}