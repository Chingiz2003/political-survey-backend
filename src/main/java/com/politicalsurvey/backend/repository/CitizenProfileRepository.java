package com.politicalsurvey.backend.repository;

import com.politicalsurvey.backend.entity.Citizen;
import com.politicalsurvey.backend.entity.CitizenProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CitizenProfileRepository extends JpaRepository<CitizenProfile, UUID> {
    boolean existsByCitizen(Citizen citizen);
    boolean existsByCitizenId(Integer citizenId);
    Optional<CitizenProfile> findByCitizenId(Integer citizenId);
}
