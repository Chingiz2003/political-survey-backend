package com.politicalsurvey.backend.controller;

import com.politicalsurvey.backend.service.FaceRecognitionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/face")
public class FaceRecognitionController {

    private final FaceRecognitionService faceRecognitionService;

    public FaceRecognitionController(FaceRecognitionService faceRecognitionService) {
        this.faceRecognitionService = faceRecognitionService;
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyFace(@RequestParam("file") MultipartFile file, @RequestParam("iin") String iin) {
        try {
            // Сохраняем файл во временную папку
            File tempFile = File.createTempFile("face_scan_", ".jpg");
            file.transferTo(tempFile);

            // Отправляем фото и ИИН в Flask
            String response = faceRecognitionService.recognizeFace(tempFile, iin);

            // Удаляем временный файл
            tempFile.delete();

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("{\"error\": \"Ошибка загрузки фото\"}");
        }
    }
}


