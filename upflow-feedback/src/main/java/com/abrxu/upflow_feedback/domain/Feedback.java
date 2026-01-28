package com.abrxu.upflow_feedback.domain;

import java.time.Instant;
import java.util.UUID;

public record Feedback(
        UUID id,
        String message,
        int rating,
        UUID departmentId,
        Instant createdAt
) {
    public Feedback {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("message cannot be blank");
        }
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("rating must be between 1 and 5");
        }
        if (departmentId == null) {
            throw new IllegalArgumentException("departmentId cannot be null");
        }
        if (createdAt == null) {
            throw new IllegalArgumentException("createdAt cannot be null");
        }
    }

    public static Feedback create(String message, int rating, UUID departmentId) {
        return new Feedback(UUID.randomUUID(), message, rating, departmentId, Instant.now());
    }
}