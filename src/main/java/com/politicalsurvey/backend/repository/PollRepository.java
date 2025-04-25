package com.politicalsurvey.backend.repository;

import com.politicalsurvey.backend.entity.Poll;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PollRepository extends JpaRepository<Poll, UUID> {
    List<Poll> findByAdminId(Integer adminId);

    @EntityGraph(attributePaths = {"questions", "questions.answerOptions"})
    Optional<Poll> findWithQuestionsAndOptionsById(UUID id);

}
