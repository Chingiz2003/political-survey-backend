package com.politicalsurvey.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "blockchain_records")
@Data
@RequiredArgsConstructor
public class BlockchainRecord {
    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecordType recordType;

    @Column(length = 255, nullable = false)
    private String blockchainHash;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "vote_id", unique = true)
    private Vote vote;

    @OneToOne
    @JoinColumn(name = "poll_id", unique = true)
    private Poll poll;

    public enum RecordType {
        POLL_CREATION, VOTE
    }
}
