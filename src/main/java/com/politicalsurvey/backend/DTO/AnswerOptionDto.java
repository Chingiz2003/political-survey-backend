package com.politicalsurvey.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerOptionDto {
    private UUID id;
    private String text;
    private int orderIndex;
    private UUID questionId;

    //Дополнительный конструктор без questionId для публичного API
    public AnswerOptionDto(UUID id, String text, int orderIndex) {
        this.id = id;
        this.text = text;
        this.orderIndex = orderIndex;
    }
}
