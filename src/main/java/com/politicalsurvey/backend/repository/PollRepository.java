package com.politicalsurvey.backend.repository;

import com.politicalsurvey.backend.entity.Poll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PollRepository extends JpaRepository<Poll, UUID> {
}
