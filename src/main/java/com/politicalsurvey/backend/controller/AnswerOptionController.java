package com.politicalsurvey.backend.controller;

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
    public List<AnswerOption> getAnswerOptionsByQuestion(@PathVariable UUID questionId) {
        return answerOptionService.getAnswerOptionsByQuestion(questionId);
    }

    @PostMapping
    public AnswerOption createAnswerOption(@RequestBody AnswerOption answerOption) {
        return answerOptionService.createAnswerOption(answerOption);
    }

    @DeleteMapping("/{id}")
    public void deleteAnswerOption(@PathVariable UUID id) {
        answerOptionService.deleteAnswerOption(id);
    }
}
