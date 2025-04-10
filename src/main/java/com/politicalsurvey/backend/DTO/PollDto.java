package com.politicalsurvey.backend.DTO;

import com.politicalsurvey.backend.entity.Poll;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class PollDto {
    private UUID id;
    private String title;
    private String description;
    private boolean anonymous;
    private Poll.PollStatus status;
    private LocalDateTime createdAt;

    public static PollDto fromEntity(Poll poll) {
        return new PollDto(
                poll.getId(),
                poll.getTitle(),
                poll.getDescription(),
                poll.isAnonymous(),
                poll.getStatus(),
                poll.getCreatedAt()
        );
    }
}
