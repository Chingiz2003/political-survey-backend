package com.politicalsurvey.backend.service;

import com.politicalsurvey.backend.DTO.AnswerSubmissionDto;
import com.politicalsurvey.backend.DTO.VoteRequestDto;
import com.politicalsurvey.backend.entity.*;
import com.politicalsurvey.backend.repository.AnswerOptionRepository;
import com.politicalsurvey.backend.repository.CitizenRepository;
import com.politicalsurvey.backend.repository.QuestionRepository;
import com.politicalsurvey.backend.repository.VoteRepository;
import com.politicalsurvey.backend.util.BlockchainUtils;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final QuestionRepository questionRepository;
    private final AnswerOptionRepository answerOptionRepository;
    private final CitizenRepository citizenRepository;
    private final BlockchainService blockchainService;

    public VoteService(VoteRepository voteRepository,
                       QuestionRepository questionRepository,
                       AnswerOptionRepository answerOptionRepository,
                       CitizenRepository citizenRepository,
                       BlockchainService blockchainService) {
        this.voteRepository = voteRepository;
        this.questionRepository = questionRepository;
        this.answerOptionRepository = answerOptionRepository;
        this.citizenRepository = citizenRepository;
        this.blockchainService = blockchainService;
    }


    @Transactional
    public void submitVote(Citizen citizen, VoteRequestDto dto) {
        for (AnswerSubmissionDto answer : dto.getAnswers()) {
            Question question = questionRepository.findById(answer.getQuestionId())
                    .orElseThrow(() -> new RuntimeException("Вопрос не найден"));

            Vote vote = new Vote();
            vote.setCitizen(citizen);
            vote.setQuestion(question);

            switch (question.getQuestionType()) {
                case TEXT -> vote.setOpenAnswer(answer.getOpenAnswer());
                case SINGLE_CHOICE, MULTIPLE_CHOICE -> {
                    AnswerOption selected = answerOptionRepository.findById(answer.getAnswerOptionId())
                            .orElseThrow(() -> new RuntimeException("Вариант ответа не найден"));
                    vote.setSelectedAnswer(selected);
                }
            }

            voteRepository.save(vote);

            // Хэшируем содержимое голоса
            String dataToHash = "citizen:" + citizen.getId() +
                    "|question:" + question.getId() +
                    "|answer:" + (vote.getOpenAnswer() != null ? vote.getOpenAnswer() : vote.getSelectedAnswer().getId());

            String txHash = blockchainService.sendToBlockchain(dataToHash);

            BlockchainRecord record = new BlockchainRecord();
            record.setRecordType(BlockchainRecord.RecordType.VOTE);
            record.setBlockchainHash(txHash);
            record.setVote(vote);
            vote.setBlockchainRecord(record); // связь для каскадного сохранения
        }
    }


    public boolean hasCitizenVoted(Integer citizenId, UUID questionId) {
        return voteRepository.existsByCitizenIdAndQuestionId(citizenId, questionId);
    }

    public List<UUID> getVotedQuestionIds(Integer citizenId) {
        return voteRepository.findByCitizenId(citizenId).stream()
                .map(vote -> vote.getQuestion().getId())
                .distinct()
                .toList();
    }

}
