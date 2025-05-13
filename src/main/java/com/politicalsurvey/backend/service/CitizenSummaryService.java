package com.politicalsurvey.backend.service;

import com.politicalsurvey.backend.entity.CitizenProfile;
import com.politicalsurvey.backend.repository.CitizenProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CitizenSummaryService {

    private final CitizenProfileRepository citizenProfileRepository;

    public Map<String, Object> generateSummary(Integer citizenId) {
        CitizenProfile profile = citizenProfileRepository.findByCitizenId(citizenId)
                .orElseThrow(() -> new IllegalArgumentException("Профиль не найден"));

        Map<String, Integer> traits = new HashMap<>();

        // Идеология
        int ideology = switch (profile.getIdeology()) {
            case "Либеральные" -> 100;
            case "Центристские" -> 60;
            case "Социалистические" -> 50;
            case "Консервативные" -> 20;
            default -> 40;
        };

        // Реформизм
        int reformism = 0;
        int changeCount = profile.getDesiredChanges() != null ? profile.getDesiredChanges().size() : 0;
        if ("Максимальная свобода граждан".equals(profile.getGovernanceStyle()) && changeCount >= 2) {
            reformism = 90;
        } else if ("Баланс свободы и дисциплины".equals(profile.getGovernanceStyle()) && changeCount >= 1) {
            reformism = 70;
        } else if ("Жёсткий порядок и контроль".equals(profile.getGovernanceStyle())) {
            reformism = 30;
        } else {
            reformism = 50;
        }

        // Патриотизм
        int patriotism = 50;
        if ("Очень горжусь".equals(profile.getNationalPride()) && "Нет, я хочу жить в Казахстане".equals(profile.getEmigrationIntent())) {
            patriotism = 100;
        } else if ("Скорее горжусь".equals(profile.getNationalPride()) && profile.getEmigrationIntent().contains("улучшатся")) {
            patriotism = 75;
        } else if (profile.getNationalPride().contains("не испытываю") || profile.getEmigrationIntent().contains("эмиграцию")) {
            patriotism = 40;
        }

        // Доверие к власти
        int trust = switch (profile.getTrustLevel()) {
            case "Полностью доверяю" -> 100;
            case "Частично доверяю" -> 70;
            case "Не доверяю" -> 30;
            default -> 50;
        };

        // Активность
        int activity = 50;
        if ("Хочу активно участвовать".equals(profile.getCivicParticipation()) && "Каждый раз".equals(profile.getVoteParticipation())) {
            activity = 100;
        } else if ("Готов только голосовать в онлайн-опросах".equals(profile.getCivicParticipation()) && "Иногда".equals(profile.getVoteParticipation())) {
            activity = 75;
        } else if ("Пока не готов/не считаю это эффективным".equals(profile.getCivicParticipation()) || "Никогда".equals(profile.getVoteParticipation())) {
            activity = 30;
        } else {
            activity = 60;
        }

        traits.put("Идеология", ideology);
        traits.put("Реформизм", reformism);
        traits.put("Патриотизм", patriotism);
        traits.put("Доверие к власти", trust);
        traits.put("Активность", activity);

        String summaryText = "Вы — гражданин с умеренными политическими взглядами и уровнем вовлечённости в общественную жизнь. На основе ваших ответов система определила ваш идеологический и гражданский профиль."
                + " Это позволит системе предлагать рекомендации, соответствующие вашим убеждениям и приоритетам.";

        Map<String, Object> result = new HashMap<>();
        result.put("summaryText", summaryText);
        result.put("traits", traits);
        return result;
    }
}
