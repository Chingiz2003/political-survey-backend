package com.politicalsurvey.backend.service;

import com.politicalsurvey.backend.DTO.IntroSurveyRequestDto;
import com.politicalsurvey.backend.entity.Citizen;
import com.politicalsurvey.backend.entity.CitizenProfile;
import com.politicalsurvey.backend.repository.CitizenProfileRepository;
import com.politicalsurvey.backend.repository.CitizenRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CitizenProfileService {

    private final CitizenRepository citizenRepository;

    private final CitizenProfileRepository citizenProfileRepository;

    public void saveProfile(Integer citizenId, IntroSurveyRequestDto dto) {
        Citizen citizen = citizenRepository.findById(citizenId)
                .orElseThrow(() -> new EntityNotFoundException("Citizen not found"));

        if (citizenProfileRepository.existsByCitizen(citizen)) {
            throw new IllegalStateException("Profile already exists");
        }

        CitizenProfile profile = new CitizenProfile();
        profile.setCitizen(citizen);
        profile.setSpecialization(dto.getSpecialization());
        profile.setIdeology(dto.getIdeology());
        profile.setAllianceOpinion(dto.getAllianceOpinion());
        profile.setDevelopmentModel(dto.getDevelopmentModel());
        profile.setPriorityIssues(dto.getPriorityIssues());
        profile.setDesiredChanges(dto.getDesiredChanges());
        profile.setVoteParticipation(dto.getVoteParticipation());
        profile.setVolunteerWillingness(dto.getVolunteerWillingness());
        profile.setInfoSource(dto.getInfoSource());
        profile.setCivicParticipation(dto.getCivicParticipation());

        citizenProfileRepository.save(profile);

        citizen.setHasCompletedIntroSurvey(true);
        citizenRepository.save(citizen);
    }

    public boolean hasCompleted(Integer citizenId) {
        return citizenProfileRepository.existsByCitizenId(citizenId);
    }


}
