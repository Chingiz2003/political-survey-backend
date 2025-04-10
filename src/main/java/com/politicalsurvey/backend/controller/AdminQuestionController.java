package com.politicalsurvey.backend.controller;

import com.politicalsurvey.backend.entity.Poll;
import com.politicalsurvey.backend.entity.Question;
import com.politicalsurvey.backend.repository.PollRepository;
import com.politicalsurvey.backend.repository.QuestionRepository;
import com.politicalsurvey.backend.service.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/questions")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AdminQuestionController {

    private final PollRepository pollRepository;

    private final QuestionRepository questionRepository;

    public AdminQuestionController(PollRepository pollRepository, QuestionRepository questionRepository) {
        this.pollRepository = pollRepository;
        this.questionRepository = questionRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createQuestion(
            @RequestParam("pollId") UUID pollId,
            @RequestBody Question question
    ) {
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new RuntimeException("Poll not found"));

        question.setPoll(poll);
        Question saved = questionRepository.save(question);

        return ResponseEntity.ok(saved);
    }
}

