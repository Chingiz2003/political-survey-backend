package com.politicalsurvey.backend.DTO;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class IntroSurveyRequestDto {

    private String specialization;
    private String ideology;
    private String allianceOpinion;
    private String developmentModel;
    private List<String> priorityIssues;
    private List<String> desiredChanges;
    private String voteParticipation;
    private String volunteerWillingness;
    private String infoSource;
    private String civicParticipation;
}
