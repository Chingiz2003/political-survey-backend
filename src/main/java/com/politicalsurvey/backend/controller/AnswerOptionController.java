package com.politicalsurvey.backend.controller;

import com.politicalsurvey.backend.DTO.AnswerOptionDto;
import com.politicalsurvey.backend.entity.AnswerOption;
import com.politicalsurvey.backend.service.AnswerOptionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/answer-options")
public class AnswerOptionController {
    private final AnswerOptionService answerOptionService;

    public AnswerOptionController(AnswerOptionService answerOptionService) {
        this.answerOptionService = answerOptionService;
    }

    @GetMapping("/question/{questionId}")
    public List<AnswerOptionDto> getAnswerOptionsByQuestion(@PathVariable UUID questionId) {
        return answerOptionService.getAnswerOptionsByQuestion(questionId).stream()
                .map(opt -> new AnswerOptionDto(opt.getId(), opt.getText(), opt.getOrderIndex(), opt.getQuestion().getId()))
                .toList();
    }


    @PostMapping
    public AnswerOption createAnswerOption(@RequestBody AnswerOptionDto dto) {
        return answerOptionService.createAnswerOption(dto);
    }


    @DeleteMapping("/{id}")
    public void deleteAnswerOption(@PathVariable UUID id) {
        answerOptionService.deleteAnswerOption(id);
    }
}
