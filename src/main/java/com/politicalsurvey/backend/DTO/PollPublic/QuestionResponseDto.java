// QuestionResponseDto.java
package com.politicalsurvey.backend.DTO.PollPublic;

import com.politicalsurvey.backend.DTO.AnswerOptionDto;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class QuestionResponseDto {
    private UUID id;
    private String text;
    private String questionType;
    private List<AnswerOptionDto> options;
}
