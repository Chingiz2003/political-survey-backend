package com.politicalsurvey.backend.controller;

import com.politicalsurvey.backend.DTO.PollDto;
import com.politicalsurvey.backend.entity.Admin;
import com.politicalsurvey.backend.entity.Poll;
import com.politicalsurvey.backend.repository.PollRepository;
import com.politicalsurvey.backend.security.AdminDetails;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/polls")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AdminPollController {

    private final PollRepository pollRepository;

    public AdminPollController(PollRepository pollRepository) {
        this.pollRepository = pollRepository;
    }

//    @Transactional
//    @PostMapping
//    public ResponseEntity<Poll> createPoll(@RequestBody Poll poll) {
//        poll.setCreatedAt(LocalDateTime.now());
//        return ResponseEntity.ok(pollRepository.save(poll));
//    }

    @Transactional
    @PostMapping("/create")
    public ResponseEntity<?> createPoll(@RequestBody Poll poll) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("Аутентификация: " + auth.getName());

            Admin admin = ((AdminDetails) auth.getPrincipal()).getAdmin();
            System.out.println("Найден админ: " + admin.getUsername() + ", ID: " + admin.getId());

            poll.setAdmin(admin);
            System.out.println("Установлен админ для опроса");

            // Установка необходимых значений по умолчанию
            if (poll.getStatus() == null) {
                poll.setStatus(Poll.PollStatus.DRAFT);
            }

            System.out.println("Сохранение опроса: " + poll.getTitle());
            Poll savedPoll = pollRepository.save(poll);
            System.out.println("Опрос успешно сохранен с ID: " + savedPoll.getId());

            return ResponseEntity.ok(savedPoll);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Ошибка при создании опроса: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при создании опроса: " + e.getMessage());
        }
    }



    @GetMapping
    public ResponseEntity<List<PollDto>> getMyPolls(Authentication auth) {
        Admin admin = ((AdminDetails) auth.getPrincipal()).getAdmin();
        List<Poll> polls = pollRepository.findByAdminId(admin.getId());

        List<PollDto> result = polls.stream()
                .map(PollDto::fromEntity)
                .toList();

        return ResponseEntity.ok(result);
    }

    @PutMapping("/close/{pollId}")
    public ResponseEntity<?> closePoll(@PathVariable UUID pollId) {
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new RuntimeException("Опрос не найден"));

        if (poll.getStatus() != Poll.PollStatus.ACTIVE) {
            return ResponseEntity.badRequest().body("Опрос не активен и не может быть завершён");
        }

        poll.setStatus(Poll.PollStatus.CLOSED);
        pollRepository.save(poll);

        return ResponseEntity.ok("Опрос успешно завершён");
    }



}
