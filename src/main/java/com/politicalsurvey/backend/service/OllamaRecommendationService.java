package com.politicalsurvey.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.politicalsurvey.backend.entity.CitizenProfile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class OllamaRecommendationService {

    public final RestTemplate restTemplate = new RestTemplate();

    public String generateRecommendation(CitizenProfile profile, String pollTitle, List<String> options) {

        String prompt = buildPrompt(profile, pollTitle, options);

        String response = restTemplate.postForObject(
                "http://localhost:11434/v1/chat/completions",
                Map.of(
                        "model", "llama3.2",
                        "messages", List.of(Map.of("role", "user", "content", prompt))
                ),
                String.class
        );

        return extractContent(response);
    }

    private String buildPrompt(CitizenProfile profile, String pollTitle, List<String> options) {
        return """
                Гражданин прошёл вводный опрос. Его профиль:
                — Специализация: %s
                — Политические взгляды: %s
                — Основные проблемы: %s
                — Желаемые изменения: %s

                Ему показан опрос: "%s"
                С такими вариантами ответов: %s

                На основе профиля, предложи самый подходящий вариант ответа и обоснуй рекомендацию.
                Ответ должен быть кратким и понятным пользователю.
                """.formatted(
                profile.getSpecialization(),
                profile.getIdeology(),
                profile.getPriorityIssues(),
                profile.getDesiredChanges(),
                pollTitle,
                options
        );
    }

    private String extractContent(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);
            return root.path("choices").get(0).path("message").path("content").asText();
        } catch (Exception e) {
            return "Ошибка обработки ответа от Ollama.";
        }
    }
}
