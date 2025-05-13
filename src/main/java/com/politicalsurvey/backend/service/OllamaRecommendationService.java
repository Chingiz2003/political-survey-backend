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
— Сфера деятельности: %s
— Политические взгляды: %s
— Предпочитаемый стиль управления: %s
— Уровень доверия к государственным институтам: %s
— Намерение уехать из Казахстана: %s
— Гордость за гражданство: %s
— Отношение к культуре и языку: %s
— Основные проблемы: %s
— Желаемые изменения: %s

Ему показан опрос: "%s"
С такими вариантами ответов:
- %s

Сформируй ответ на **русском литературном языке**. Избегай использования иностранных слов и сокращений.

Ответ должен быть в следующем формате:
— Рекомендуемый вариант: [название]
— Обоснование: [краткое объяснение, почему этот вариант соответствует взглядам гражданина]
""".formatted(
                profile.getSpecialization(),
                profile.getIdeology(),
                profile.getGovernanceStyle(),
                profile.getTrustLevel(),
                profile.getEmigrationIntent(),
                profile.getNationalPride(),
                profile.getCultureImportance(),
                profile.getPriorityIssues(),
                profile.getDesiredChanges(),
                pollTitle,
                String.join("\n- ", options)
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
