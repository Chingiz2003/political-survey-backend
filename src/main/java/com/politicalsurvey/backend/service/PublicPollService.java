package com.politicalsurvey.backend.service;

import com.politicalsurvey.backend.DTO.AnswerOptionDto;
import com.politicalsurvey.backend.DTO.PollPublic.PollResponseDto;
import com.politicalsurvey.backend.DTO.PollPublic.QuestionResponseDto;
import com.politicalsurvey.backend.repository.AnswerOptionRepository;
import com.politicalsurvey.backend.repository.PollRepository;
import com.politicalsurvey.backend.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PublicPollService {

    private final PollRepository pollRepository;
    private final QuestionRepository questionRepository;
    private final AnswerOptionRepository answerOptionRepository;

    public PublicPollService(PollRepository pollRepository,
                             QuestionRepository questionRepository,
                             AnswerOptionRepository answerOptionRepository) {
        this.pollRepository = pollRepository;
        this.questionRepository = questionRepository;
        this.answerOptionRepository = answerOptionRepository;
    }

    public List<PollResponseDto> getAllPollsWithQuestionsAndOptions() {
        return pollRepository.findAll().stream().map(poll -> {
            PollResponseDto dto = new PollResponseDto();
            dto.setId(poll.getId());
            dto.setTitle(poll.getTitle());
            dto.setDescription(poll.getDescription());
            dto.setAnonymous(poll.isAnonymous());
            dto.setStatus(poll.getStatus().name());

            List<QuestionResponseDto> questionDtos = poll.getQuestions().stream().map(q -> {
                QuestionResponseDto qDto = new QuestionResponseDto();
                qDto.setId(q.getId());
                qDto.setText(q.getText());
                qDto.setQuestionType(q.getQuestionType().name());

                List<AnswerOptionDto> options = q.getAnswerOptions().stream()
                        .map(opt -> new AnswerOptionDto(opt.getId(), opt.getText(), opt.getOrderIndex(), q.getId()))
                        .toList();
                qDto.setOptions(options);

                return qDto;
            }).toList();

            dto.setQuestions(questionDtos);
            return dto;
        }).toList();
    }

    public PollResponseDto getPollById(UUID pollId) {
        return pollRepository.findById(pollId)
                .map(poll -> {
                    PollResponseDto dto = new PollResponseDto();
                    dto.setId(poll.getId());
                    dto.setTitle(poll.getTitle());
                    dto.setDescription(poll.getDescription());
                    dto.setAnonymous(poll.isAnonymous());
                    dto.setStatus(poll.getStatus().name());

                    List<QuestionResponseDto> questionDtos = poll.getQuestions().stream().map(q -> {
                        QuestionResponseDto qDto = new QuestionResponseDto();
                        qDto.setId(q.getId());
                        qDto.setText(q.getText());
                        qDto.setQuestionType(q.getQuestionType().name());

                        List<AnswerOptionDto> options = q.getAnswerOptions().stream()
                                .map(opt -> new AnswerOptionDto(opt.getId(), opt.getText(), opt.getOrderIndex(), q.getId()))
                                .toList();
                        qDto.setOptions(options);

                        return qDto;
                    }).toList();

                    dto.setQuestions(questionDtos);
                    return dto;
                })
                .orElseThrow(() -> new RuntimeException("Опрос не найден"));
    }

}

