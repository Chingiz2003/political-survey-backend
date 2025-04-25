package com.politicalsurvey.backend.controller;

import com.politicalsurvey.backend.DTO.PollPublic.PollResponseDto;
import com.politicalsurvey.backend.entity.CitizenProfile;
import com.politicalsurvey.backend.entity.Poll;
import com.politicalsurvey.backend.entity.Question;
import com.politicalsurvey.backend.entity.Vote;
import com.politicalsurvey.backend.repository.PollRepository;
import com.politicalsurvey.backend.service.PublicPollService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/polls")
@CrossOrigin(origins = "http://localhost:3000")
public class PublicPollController {

    private final PublicPollService publicPollService;

    private final PollRepository pollRepository;

    public PublicPollController(PublicPollService publicPollService,
                                PollRepository pollRepository) {
        this.publicPollService = publicPollService;
        this.pollRepository = pollRepository;
    }

    @GetMapping("/public")
    public ResponseEntity<List<PollResponseDto>> getAllPollsForCitizens() {
        return ResponseEntity.ok(publicPollService.getAllPollsWithQuestionsAndOptions());
    }

    @GetMapping("/public/{pollId}")
    public ResponseEntity<PollResponseDto> getPollById(@PathVariable UUID pollId) {
        return ResponseEntity.ok(publicPollService.getPollById(pollId));
    }

    @GetMapping("/results/{pollId}")
    public ResponseEntity<?> getPollResults(@PathVariable UUID pollId) {
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new RuntimeException("Poll not found"));

        List<Question> questions = poll.getQuestions();

        var result = questions.stream().map(q -> {
            var map = new HashMap<String, Object>();
            map.put("question", q.getText());
            map.put("type", q.getQuestionType());

            if (q.getQuestionType() == Question.QuestionType.TEXT) {
                List<String> answers = q.getVotes().stream()
                        .map(Vote::getOpenAnswer)
                        .toList();
                map.put("answers", answers);
            } else {
                Map<String, Long> counts = q.getVotes().stream()
                        .filter(v -> v.getSelectedAnswer() != null)
                        .collect(Collectors.groupingBy(
                                v -> v.getSelectedAnswer().getText(),
                                Collectors.counting()
                        ));
                map.put("answers", counts);
            }

            return map;
        }).toList();

        return ResponseEntity.ok(result);
    }


}
