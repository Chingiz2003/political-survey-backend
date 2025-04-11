package com.politicalsurvey.backend.service;

import com.politicalsurvey.backend.DTO.AnswerOptionDto;
import com.politicalsurvey.backend.entity.AnswerOption;
import com.politicalsurvey.backend.entity.Question;
import com.politicalsurvey.backend.repository.AnswerOptionRepository;
import com.politicalsurvey.backend.repository.QuestionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AnswerOptionService {

    private final AnswerOptionRepository answerOptionRepository;
    private final QuestionRepository questionRepository;

    public AnswerOptionService(AnswerOptionRepository answerOptionRepository, QuestionRepository questionRepository) {
        this.answerOptionRepository = answerOptionRepository;
        this.questionRepository = questionRepository;
    }

    @Transactional
    public AnswerOption createAnswerOption(AnswerOptionDto dto) {
        // Проверь это логированием
        System.out.println("DTO пришёл: " + dto);

        Question question = questionRepository.findById(dto.getQuestionId())
                .orElseThrow(() -> new RuntimeException("Вопрос не найден: " + dto.getQuestionId()));

        AnswerOption option = new AnswerOption();
        option.setText(dto.getText());
        option.setOrderIndex(dto.getOrderIndex());
        option.setQuestion(question);

        return answerOptionRepository.save(option);
    }


    public List<AnswerOption> getAnswerOptionsByQuestion(UUID questionId) {
        return answerOptionRepository.findByQuestionId(questionId);
    }

    public void deleteAnswerOption(UUID id) {
        answerOptionRepository.deleteById(id);
    }

}

