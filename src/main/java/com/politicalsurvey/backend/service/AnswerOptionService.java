package com.politicalsurvey.backend.service;

import com.politicalsurvey.backend.entity.AnswerOption;
import com.politicalsurvey.backend.repository.AnswerOptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AnswerOptionService {
    private final AnswerOptionRepository answerOptionRepository;

    public AnswerOptionService(AnswerOptionRepository answerOptionRepository) {
        this.answerOptionRepository = answerOptionRepository;
    }

    public List<AnswerOption> getAllAnswerOptions() {
        return answerOptionRepository.findAll();
    }

    public List<AnswerOption> getAnswerOptionsByQuestion(UUID questionId) {
        return answerOptionRepository.findByQuestionId(questionId);
    }

    public AnswerOption getAnswerOptionById(UUID id) {
        return answerOptionRepository.findById(id).orElseThrow(() -> new RuntimeException("Вариант ответа не найден"));
    }

    public AnswerOption createAnswerOption(AnswerOption answerOption) {
        return answerOptionRepository.save(answerOption);
    }

    public AnswerOption updateAnswerOption(UUID id, AnswerOption answerOptionDetails) {
        AnswerOption answerOption = getAnswerOptionById(id);
        answerOption.setText(answerOptionDetails.getText());
        answerOption.setOrderIndex(answerOptionDetails.getOrderIndex());
        return answerOptionRepository.save(answerOption);
    }

    public void deleteAnswerOption(UUID id) {
        answerOptionRepository.deleteById(id);
    }
}

