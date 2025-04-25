package com.politicalsurvey.backend.controller;

import com.politicalsurvey.backend.DTO.IntroSurveyRequestDto;
import com.politicalsurvey.backend.service.CitizenProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;



import java.util.UUID;

@RestController
@RequestMapping("/api/intro-survey")
@RequiredArgsConstructor
public class IntroSurveyController {

    private final CitizenProfileService citizenProfileService;

    @PostMapping("/{citizenId}")
    public ResponseEntity<?> saveSurvey(
            @PathVariable Integer citizenId,
            @RequestBody @Valid IntroSurveyRequestDto dto) {
        citizenProfileService.saveProfile(citizenId, dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{citizenId}/status")
    public ResponseEntity<Boolean> hasCompleted(@PathVariable Integer citizenId) {
        return ResponseEntity.ok(citizenProfileService.hasCompleted(citizenId));
    }

}

