package com.politicalsurvey.backend.repository;

import com.politicalsurvey.backend.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VoteRepository extends JpaRepository<Vote, UUID> {
    List<Vote> findBySelectedAnswer_Id(UUID answerId);
    boolean existsByCitizenIdAndQuestionId(Integer citizenId, UUID questionId);
    List<Vote> findByCitizenId(Integer citizenId);
}
