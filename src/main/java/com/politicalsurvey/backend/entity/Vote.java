package com.politicalsurvey.backend.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "votes")
@Data
@RequiredArgsConstructor
public class Vote {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ManyToOne
    @JoinColumn(name = "answer_id")
    private AnswerOption selectedAnswer;

    @Column(columnDefinition = "TEXT")
    private String openAnswer;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime votedAt;

    @ManyToOne
    @JoinColumn(name = "citizen_id", nullable = false)
    private Citizen citizen;

    @OneToOne(mappedBy = "vote", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private BlockchainRecord blockchainRecord;
}

