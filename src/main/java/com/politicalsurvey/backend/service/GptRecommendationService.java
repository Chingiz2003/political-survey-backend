//package com.politicalsurvey.backend.service;
//
//import com.politicalsurvey.backend.entity.CitizenProfile;
//import com.theokanning.openai.OpenAiService;
//import com.theokanning.openai.completion.CompletionRequest;
//import com.theokanning.openai.completion.CompletionResult;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@Slf4j
//public class GptRecommendationService {
//
//    private final OpenAiService openAiService;
//
//    public GptRecommendationService(@Value("${openai.api.key}") String openAiKey) {
//        this.openAiService = new OpenAiService(openAiKey);
//    }
//
//    public String generateRecommendation(CitizenProfile profile, String pollTitle, List<String> options) {
//        String prompt = buildPrompt(profile, pollTitle, options);
//
//        try {
//            CompletionRequest request = CompletionRequest.builder()
//                    .model("gpt-3.5-turbo-instruct")
//                    .prompt(prompt)
//                    .maxTokens(250)
//                    .temperature(0.7)
//                    .build();
//
//            CompletionResult result = openAiService.createCompletion(request);
//            return result.getChoices().get(0).getText().trim();
//
//        } catch (Exception e) {
//            log.error("GPT API error: {}", e.getMessage(), e);
//            throw new RuntimeException("Ошибка получения рекомендации от GPT", e);
//        }
//    }
//
//
//    private String buildPrompt(CitizenProfile profile, String pollTitle, List<String> options) {
//        return """
//                Гражданин прошёл вводный опрос. Его профиль:
//                — Специализация: %s
//                — Политические взгляды: %s
//                — Основные проблемы: %s
//                — Желаемые изменения: %s
//
//                Ему показан опрос: "%s"
//                С такими вариантами ответов: %s
//
//                На основе профиля, предложи самый подходящий вариант ответа и обоснуй рекомендацию.
//                Ответ должен быть кратким и понятным пользователю.
//                """.formatted(
//                profile.getSpecialization(),
//                profile.getIdeology(),
//                profile.getPriorityIssues(),
//                profile.getDesiredChanges(),
//                pollTitle,
//                options
//        );
//    }
//}
