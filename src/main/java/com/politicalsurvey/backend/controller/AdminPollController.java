package com.politicalsurvey.backend.controller;

import com.politicalsurvey.backend.entity.Poll;
import com.politicalsurvey.backend.repository.PollRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/admin/polls")
public class AdminPollController {

    private final PollRepository pollRepository;

    public AdminPollController(PollRepository pollRepository) {
        this.pollRepository = pollRepository;
    }

    @PostMapping
    public ResponseEntity<Poll> createPoll(@RequestBody Poll poll) {
        poll.setCreatedAt(LocalDateTime.now());
        return ResponseEntity.ok(pollRepository.save(poll));
    }
}
