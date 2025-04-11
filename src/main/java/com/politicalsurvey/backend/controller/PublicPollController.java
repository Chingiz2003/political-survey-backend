package com.politicalsurvey.backend.controller;

import com.politicalsurvey.backend.DTO.PollPublic.PollResponseDto;
import com.politicalsurvey.backend.service.PublicPollService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
