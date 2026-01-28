package com.abrxu.upflow_feedback.infra;

import com.abrxu.upflow_feedback.domain.Feedback;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

    public Feedback toDomain() {
        return new Feedback(id, message, rating, departmentId, createdAt);
    }

    public static FeedbackEntity fromDomain(Feedback feedback) {
        return new FeedbackEntity(
                feedback.id(),
                feedback.message(),
                feedback.rating(),
                feedback.departmentId(),
                feedback.createdAt()
        );
    }
}
