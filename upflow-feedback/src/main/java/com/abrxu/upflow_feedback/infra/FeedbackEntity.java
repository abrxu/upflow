package com.abrxu.upflow_feedback.infra;

import com.abrxu.upflow_feedback.domain.Feedback;
import com.abrxu.upflow_feedback.domain.FeedbackStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tbl_feedback")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackEntity {

    @Id
    private UUID id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(nullable = false)
    private int rating;

    @Column(nullable = false)
    private UUID departmentId;

    @Column(nullable = false)
    private Instant createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FeedbackStatus status;

    @Column(columnDefinition = "TEXT")
    private String moderatedContent;

    public Feedback toDomain() {
        return new Feedback(id, message, rating, departmentId, createdAt, status, moderatedContent);
    }

    public static FeedbackEntity fromDomain(Feedback feedback) {
        return new FeedbackEntity(
                feedback.id(),
                feedback.message(),
                feedback.rating(),
                feedback.departmentId(),
                feedback.createdAt(),
                feedback.status(),
                feedback.moderatedContent()
        );
    }
}
