package com.politicalsurvey.backend.controller;

import com.politicalsurvey.backend.service.CitizenSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/summary")
@RequiredArgsConstructor
public class CitizenSummaryController {

    private final CitizenSummaryService citizenSummaryService;

    @GetMapping("/{citizenId}")
    public ResponseEntity<?> getCitizenSummary(@PathVariable Integer citizenId) {
        return ResponseEntity.ok(citizenSummaryService.generateSummary(citizenId));
    }

}
