package com.politicalsurvey.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteRequestDto {

    private UUID pollId; // ID опроса
    private List<AnswerSubmissionDto> answers; // список ответов на вопросы
}
