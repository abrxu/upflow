package com.abrxu.upflow_feedback.application.dto.response;

import java.time.Instant;
import java.util.UUID;

public record FeedbackResponse(
        UUID id,
        String message,
        int rating,
        UUID departmentId,
        Instant createdAt
) {
}