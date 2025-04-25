package com.politicalsurvey.backend.controller;

import com.politicalsurvey.backend.DTO.QuestionDto;
import com.politicalsurvey.backend.entity.Poll;
import com.politicalsurvey.backend.entity.Question;
import com.politicalsurvey.backend.repository.PollRepository;
import com.politicalsurvey.backend.repository.QuestionRepository;
import com.politicalsurvey.backend.service.QuestionService;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/all")
    public ResponseEntity<List<QuestionDto>> getAllQuestions() {
        List<QuestionDto> dtos = questionRepository.findAll().stream()
                .map(q -> {
                    QuestionDto dto = new QuestionDto();
                    dto.setText(q.getText());
                    dto.setQuestionType(q.getQuestionType().name());
                    dto.setId(q.getId());
                    dto.setPollId(q.getPoll().getId()); //добавили pollId
                    return dto;
                })
                .toList();

        return ResponseEntity.ok(dtos);
    }




    @PostMapping("/create")
    public ResponseEntity<?> createQuestion(
            @RequestParam("pollId") UUID pollId,
            @RequestBody QuestionDto dto
    ) {
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new RuntimeException("Poll not found"));

        Question question = new Question();
        question.setText(dto.getText());
        question.setPoll(poll);

        try {
            Question.QuestionType type = Question.QuestionType.valueOf(dto.getQuestionType()); // Преобразование строки в enum
            question.setQuestionType(type);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Неверный тип вопроса: " + dto.getQuestionType());
        }

        return ResponseEntity.ok(questionRepository.save(question));
    }

    @GetMapping("/by-poll/{pollId}")
    public ResponseEntity<List<QuestionDto>> getQuestionsByPoll(@PathVariable UUID pollId) {
        List<QuestionDto> dtos = questionRepository.findByPollId(pollId).stream()
                .map(q -> {
                    QuestionDto dto = new QuestionDto();
                    dto.setId(q.getId());
                    dto.setText(q.getText());
                    dto.setQuestionType(q.getQuestionType().name());
                    dto.setPollId(q.getPoll().getId()); // получение опросов по id
                    return dto;
                })
                .toList();

        return ResponseEntity.ok(dtos);
    }


//    @PutMapping("/update/{questionId}")
//    public ResponseEntity<?> updateQuestion(@PathVariable UUID questionId, @RequestBody QuestionDto dto) {
//        Question question = questionRepository.findById(questionId)
//                .orElseThrow(() -> new RuntimeException("Вопрос не найден"));
//
//        if (question.getPoll().getStatus() == Poll.PollStatus.CLOSED) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                    .body("Нельзя редактировать вопрос завершённого опроса");
//        }
//
//        question.setText(dto.getText());
//        question.setQuestionType(Question.QuestionType.valueOf(dto.getQuestionType()));
//        questionRepository.save(question);
//
//        return ResponseEntity.ok("Вопрос успешно обновлён");
//    }

    @PutMapping("/{questionId}")
    public ResponseEntity<?> updateQuestion(@PathVariable UUID questionId, @RequestBody QuestionDto dto) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Вопрос не найден"));

        if (question.getPoll().getStatus() == Poll.PollStatus.CLOSED) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Опрос завершён и вопрос не может быть отредактирован");
        }

        question.setText(dto.getText());
        question.setQuestionType(Question.QuestionType.valueOf(dto.getQuestionType()));

        return ResponseEntity.ok(questionRepository.save(question));
    }

    @DeleteMapping("/{questionId}")
    public ResponseEntity<?> deleteQuestion(@PathVariable UUID questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Вопрос не найден"));

        if (question.getPoll().getStatus() == Poll.PollStatus.CLOSED) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Опрос завершён, удаление вопроса невозможно");
        }

        questionRepository.deleteById(questionId);
        return ResponseEntity.ok("Вопрос успешно удалён");
    }



}

