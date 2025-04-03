package com.politicalsurvey.backend.service;

import com.politicalsurvey.backend.entity.Question;
import com.politicalsurvey.backend.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public List<Question> getQuestionsByPoll(UUID pollId) {
        return questionRepository.findByPollId(pollId);
    }

    public Question getQuestionById(UUID id) {
        return questionRepository.findById(id).orElseThrow(() -> new RuntimeException("Вопрос не найден"));
    }

    public Question createQuestion(Question question) {
        return questionRepository.save(question);
    }

    public Question updateQuestion(UUID id, Question questionDetails) {
        Question question = getQuestionById(id);
        question.setText(questionDetails.getText());
        question.setQuestionType(questionDetails.getQuestionType());
        return questionRepository.save(question);
    }

    public void deleteQuestion(UUID id) {
        questionRepository.deleteById(id);
    }
}

