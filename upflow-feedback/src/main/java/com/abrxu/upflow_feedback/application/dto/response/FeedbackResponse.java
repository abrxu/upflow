package com.abrxu.upflow_feedback.application.dto.response;

import com.abrxu.upflow_feedback.domain.FeedbackStatus;

import java.time.Instant;
import java.util.UUID;

public record FeedbackResponse(
        UUID id,
        String message,
        int rating,
        UUID departmentId,
        Instant createdAt,
        FeedbackStatus status,
        String moderatedContent
) {
}
