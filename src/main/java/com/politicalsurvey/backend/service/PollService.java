package com.politicalsurvey.backend.service;

import com.politicalsurvey.backend.entity.Poll;
import com.politicalsurvey.backend.repository.PollRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PollService {
    private final PollRepository pollRepository;

    public PollService(PollRepository pollRepository) {
        this.pollRepository = pollRepository;
    }

    public List<Poll> getAllPolls() {
        return pollRepository.findAll();
    }

    public Poll getPollById(UUID id) {
        return pollRepository.findById(id).orElseThrow(() -> new RuntimeException("Опрос не найден"));
    }

    public Poll createPoll(Poll poll) {
        return pollRepository.save(poll);
    }

    public Poll updatePoll(UUID id, Poll pollDetails) {
        Poll poll = getPollById(id);
        poll.setTitle(pollDetails.getTitle());
        poll.setDescription(pollDetails.getDescription());
        poll.setStatus(pollDetails.getStatus());
        poll.setAnonymous(pollDetails.isAnonymous());
        validatePollCompleteness(poll);
        return pollRepository.save(poll);
    }

    public void deletePoll(UUID id) {
        pollRepository.deleteById(id);
    }

    public void validatePollCompleteness(Poll poll) {
        boolean hasQuestions = poll.getQuestions() != null && !poll.getQuestions().isEmpty();
        boolean allHaveOptions = hasQuestions && poll.getQuestions().stream()
                .allMatch(q -> q.getAnswerOptions() != null && !q.getAnswerOptions().isEmpty());

        if (!hasQuestions || !allHaveOptions) {
            poll.setStatus(Poll.PollStatus.DRAFT);
        }
    }
}

