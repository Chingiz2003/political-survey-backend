package com.politicalsurvey.backend.controller;

import com.politicalsurvey.backend.DTO.PollPublic.PollResponseDto;
import com.politicalsurvey.backend.service.PublicPollService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/polls")
@CrossOrigin(origins = "http://localhost:3000")
public class PublicPollController {

    private final PublicPollService publicPollService;

    public PublicPollController(PublicPollService publicPollService) {
        this.publicPollService = publicPollService;
    }

    @GetMapping("/public")
    public ResponseEntity<List<PollResponseDto>> getAllPollsForCitizens() {
        return ResponseEntity.ok(publicPollService.getAllPollsWithQuestionsAndOptions());
    }

    @GetMapping("/public/{pollId}")
    public ResponseEntity<PollResponseDto> getPollById(@PathVariable UUID pollId) {
        return ResponseEntity.ok(publicPollService.getPollById(pollId));
    }

}
