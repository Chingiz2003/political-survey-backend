package com.politicalsurvey.backend.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "answer_options")
@Data
@RequiredArgsConstructor
public class AnswerOption {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(nullable = false)
    private int orderIndex;

    @OneToMany(mappedBy = "selectedAnswer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vote> votes;

}