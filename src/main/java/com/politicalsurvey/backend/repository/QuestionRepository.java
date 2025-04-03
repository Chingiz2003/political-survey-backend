package com.politicalsurvey.backend.repository;

import com.politicalsurvey.backend.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface QuestionRepository extends JpaRepository<Question, UUID> {
    List<Question> findByPollId(UUID pollId); // Получить все вопросы по ID опроса
}
