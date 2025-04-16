package com.politicalsurvey.backend.controller;

import com.politicalsurvey.backend.DTO.VoteRequestDto;
import com.politicalsurvey.backend.entity.Citizen;
import com.politicalsurvey.backend.entity.Vote;
import com.politicalsurvey.backend.service.VoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/votes")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class VoteController {

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submitVote(@RequestBody VoteRequestDto dto, Authentication authentication) {
        Citizen citizen = (Citizen) authentication.getPrincipal();
        voteService.submitVote(citizen, dto);
        return ResponseEntity.ok("Голос принят");
    }


    @GetMapping("/has-voted")
    public ResponseEntity<?> hasVoted(
            @RequestParam UUID questionId,
            Authentication authentication
    ) {
        Citizen citizen = (Citizen) authentication.getPrincipal();
        boolean voted = voteService.hasCitizenVoted(citizen.getId(), questionId);
        return ResponseEntity.ok(voted);
    }

    @GetMapping("/voted-questions")
    public ResponseEntity<?> getVotedQuestions(Authentication authentication) {
        Citizen citizen = (Citizen) authentication.getPrincipal();
        List<UUID> votedIds = voteService.getVotedQuestionIds(citizen.getId());
        return ResponseEntity.ok(votedIds);
    }

}
