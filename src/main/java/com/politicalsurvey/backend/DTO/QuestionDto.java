package com.politicalsurvey.backend.DTO;

import com.politicalsurvey.backend.entity.Question.QuestionType;
import lombok.Data;

import java.util.UUID;

@Data
public class QuestionDto {
    private UUID id;
    private String text;
    private String questionType; // Строка из фронта: "SINGLE_CHOICE", "TEXT", и т.д.
    private UUID pollId;
}
