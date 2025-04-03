package com.politicalsurvey.backend.controller;

import com.politicalsurvey.backend.entity.Citizen;
import com.politicalsurvey.backend.repository.CitizenRepository;
import com.politicalsurvey.backend.security.JwtUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AuthController {

    private final CitizenRepository citizenRepository;
    private final JwtUtil jwtUtil;

    public AuthController(CitizenRepository citizenRepository, JwtUtil jwtUtil) {
        this.citizenRepository = citizenRepository;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        try {
            System.out.println("Received login request with IIN: " + body.get("iin"));

            String iin = body.get("iin");
            if (iin == null || iin.isEmpty()) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("error", "ИИН не может быть пустым"));
            }

            // Используем try-catch для конкретного запроса к БД
            Citizen citizen;
            try {
                Optional<Citizen> citizenOpt = citizenRepository.findByIin(iin);
                if (citizenOpt.isEmpty()) {
                    System.out.println("Citizen not found for IIN: " + iin);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(Collections.singletonMap("error", "Гражданин с указанным ИИН не найден"));
                }
                citizen = citizenOpt.get();
            } catch (DataAccessException dae) {
                System.err.println("Database error: " + dae.getMessage());
                dae.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Collections.singletonMap("error", "Ошибка при доступе к базе данных"));
            }

            System.out.println("Found citizen with ID: " + citizen.getId());

            String token = jwtUtil.generateToken(citizen.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("citizen", Map.of(
                    "id", citizen.getId(),
                    "fullName", citizen.getFullName(),
                    "iin", citizen.getIin()
            ));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Ошибка при аутентификации: " + e.getMessage()));
        }
    }

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok(Collections.singletonMap("message", "Auth controller is working!"));
    }
}