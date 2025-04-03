package com.politicalsurvey.backend.repository;

import com.politicalsurvey.backend.entity.Citizen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CitizenRepository extends JpaRepository<Citizen, Integer> {
    Optional<Citizen> findByIin(String iin);
}
