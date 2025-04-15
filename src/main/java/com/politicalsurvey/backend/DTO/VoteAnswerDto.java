package com.politicalsurvey.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteAnswerDto {
    private UUID questionId;
    private UUID selectedAnswerId;
    private String openAnswer;
}
