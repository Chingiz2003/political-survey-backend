package com.politicalsurvey.backend.controller;

import com.politicalsurvey.backend.DTO.AnswerOptionDto;
import com.politicalsurvey.backend.entity.AnswerOption;
import com.politicalsurvey.backend.entity.Poll;
import com.politicalsurvey.backend.repository.AnswerOptionRepository;
import com.politicalsurvey.backend.service.AnswerOptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/answer-options")
public class AnswerOptionController {

    private final AnswerOptionService answerOptionService;

    private final AnswerOptionRepository answerOptionRepository;

    public AnswerOptionController(AnswerOptionService answerOptionService,
                                  AnswerOptionRepository answerOptionRepository) {
        this.answerOptionService = answerOptionService;
        this.answerOptionRepository = answerOptionRepository;
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


//    @DeleteMapping("/{id}")
//    public void deleteAnswerOption(@PathVariable UUID id) {
//        answerOptionService.deleteAnswerOption(id);
//    }

//    @PutMapping("/update/{optionId}")
//    public ResponseEntity<?> updateAnswerOption(@PathVariable UUID optionId, @RequestBody AnswerOptionDto dto) {
//        AnswerOption option = answerOptionRepository.findById(optionId)
//                .orElseThrow(() -> new RuntimeException("Вариант ответа не найден"));
//
//        if (option.getQuestion().getPoll().getStatus() == Poll.PollStatus.CLOSED) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                    .body("Нельзя редактировать вариант завершённого опроса");
//        }
//
//        option.setText(dto.getText());
//        option.setOrderIndex(dto.getOrderIndex());
//        answerOptionRepository.save(option);
//
//        return ResponseEntity.ok("Вариант ответа успешно обновлён");
//    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAnswerOption(@PathVariable UUID id, @RequestBody AnswerOptionDto dto) {
        AnswerOption option = answerOptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Вариант ответа не найден"));

        if (option.getQuestion().getPoll().getStatus() == Poll.PollStatus.CLOSED) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Опрос завершён и варианты не могут быть отредактированы");
        }

        option.setText(dto.getText());
        option.setOrderIndex(dto.getOrderIndex());
        return ResponseEntity.ok(answerOptionRepository.save(option));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAnswerOption(@PathVariable UUID id) {
        AnswerOption option = answerOptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Вариант ответа не найден"));

        // Защита от удаления в завершённом опросе
        if (option.getQuestion().getPoll().getStatus() == com.politicalsurvey.backend.entity.Poll.PollStatus.CLOSED) {
            return ResponseEntity.status(403).body("Нельзя удалить вариант из завершённого опроса");
        }

        answerOptionRepository.deleteById(id);
        return ResponseEntity.ok("Вариант ответа удалён");
    }


}
