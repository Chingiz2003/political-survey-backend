package com.politicalsurvey.backend.controller;

import com.politicalsurvey.backend.entity.CitizenProfile;
import com.politicalsurvey.backend.entity.Poll;
import com.politicalsurvey.backend.entity.Question;
import com.politicalsurvey.backend.entity.AnswerOption;
import com.politicalsurvey.backend.repository.AnswerOptionRepository;
import com.politicalsurvey.backend.repository.CitizenProfileRepository;
import com.politicalsurvey.backend.repository.PollRepository;
import com.politicalsurvey.backend.repository.QuestionRepository;
//import com.politicalsurvey.backend.service.GptRecommendationService;
import com.politicalsurvey.backend.service.OllamaRecommendationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/recommendation")
@RequiredArgsConstructor
public class GptRecommendationController {

    private final PollRepository pollRepository;
    private final AnswerOptionRepository answerOptionRepository;
    private final CitizenProfileRepository citizenProfileRepository;
//    private final GptRecommendationService gptRecommendationService;
    private final QuestionRepository questionRepository;
    private final OllamaRecommendationService ollamaRecommendationService;

//    @GetMapping("/poll/{pollId}/citizen/{citizenId}")
//    public ResponseEntity<String> getRecommendation(
//            @PathVariable UUID pollId,
//            @PathVariable Integer citizenId) {
//
//        // 1. Получаем сам опрос (для названия)
//        Poll poll = pollRepository.findById(pollId)
//                .orElseThrow(() -> new EntityNotFoundException("Poll not found"));
//
//        // 2. Получаем все вопросы по опросу
//        List<Question> questions = questionRepository.findByPollId(pollId);
//
//        // 3. Собираем ID всех вопросов
//        List<UUID> questionIds = questions.stream().map(Question::getId).toList();
//
//        // 4. Получаем все варианты ответов по этим вопросам
//        List<String> options = answerOptionRepository.findByQuestionIdIn(questionIds)
//                .stream().map(AnswerOption::getText).toList();
//
//        // 5. Получаем профиль гражданина
//        CitizenProfile profile = citizenProfileRepository.findByCitizenId(citizenId)
//                .orElseThrow(() -> new EntityNotFoundException("Citizen profile not found"));
//
//        // 6. Получаем рекомендацию от GPT
//        String rec = gptRecommendationService.generateRecommendation(profile, poll.getTitle(), options);
//
//        return ResponseEntity.ok(rec);
//    }

    @GetMapping("/poll/{pollId}/citizen/{citizenId}")
    public ResponseEntity<String> getOllamaRecommendation(
            @PathVariable UUID pollId,
            @PathVariable Integer citizenId) {

        Poll poll = pollRepository.findById(pollId).orElseThrow();
        List<Question> questions = questionRepository.findByPollId(pollId);
        List<UUID> questionIds = questions.stream().map(Question::getId).toList();
        List<String> options = answerOptionRepository.findByQuestionIdIn(questionIds)
                .stream().map(AnswerOption::getText).toList();
        CitizenProfile profile = citizenProfileRepository.findByCitizenId(citizenId)
                .orElseThrow();

        String rec = ollamaRecommendationService.generateRecommendation(profile, poll.getTitle(), options);
        return ResponseEntity.ok(rec);
    }

}

