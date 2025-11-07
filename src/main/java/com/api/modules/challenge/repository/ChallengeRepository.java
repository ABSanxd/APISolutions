package com.api.modules.challenge.repository;

import com.api.modules.challenge.model.Challenge;
import com.api.common.enums.Category;
import com.api.common.enums.Frequency;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;
import com.api.common.enums.Status;


//por aqui entraremos a la tabla challenges para consultar los retos disponibles
public interface ChallengeRepository extends JpaRepository<Challenge, UUID> {
    List<Challenge> findByCategory(Category category);

    List<Challenge> findByFrequency(Frequency frequency);

    List<Challenge> findByCategoryAndFrequency(Category category, Frequency frequency);    

    List<Challenge> findByStatus(Status status);

    boolean existsByName(String name);

}

