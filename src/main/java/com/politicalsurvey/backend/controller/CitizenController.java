package com.politicalsurvey.backend.controller;

import com.politicalsurvey.backend.entity.Citizen;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/citizens")
public class CitizenController {

    @GetMapping("/me")
    public ResponseEntity<Citizen> getCurrentCitizen(@AuthenticationPrincipal Citizen citizen) {
        return ResponseEntity.ok(citizen);
    }
}
