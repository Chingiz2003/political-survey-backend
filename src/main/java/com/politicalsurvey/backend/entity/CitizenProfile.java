package com.politicalsurvey.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "citizen_profile")
@Data
@RequiredArgsConstructor
public class CitizenProfile {

    @Id
    @GeneratedValue
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "citizen_id", nullable = false, unique = true)
    private Citizen citizen;

    private String specialization;
    private String ideology;
    private String allianceOpinion;
    private String developmentModel;

    @ElementCollection
    private List<String> priorityIssues;

    @ElementCollection
    private List<String> desiredChanges;

    private String voteParticipation;
    private String volunteerWillingness;
    private String infoSource;
    private String civicParticipation;
    private String trustLevel;
    private String governanceStyle;
    private String emigrationIntent;
    private String nationalPride;
    private String cultureImportance;

}
