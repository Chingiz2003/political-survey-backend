package com.politicalsurvey.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerSubmissionDto {
    private UUID questionId;
    private UUID answerOptionId; // для SINGLE_CHOICE и MULTIPLE_CHOICE
    private String openAnswer;   // для TEXT
}
