package com.politicalsurvey.backend.DTO.PollPublic;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class PollResponseDto {
    private UUID id;
    private String title;
    private String description;
    private boolean anonymous;
    private String status;
    private List<QuestionResponseDto> questions;
}

