package com.abrxu.upflow_feedback.infra.kafka;

import com.abrxu.upflow.events.FeedbackFlaggedEvent;
import com.abrxu.upflow.events.FeedbackModeratedEvent;
import com.abrxu.upflow_feedback.application.port.out.FeedbackRepository;
import com.abrxu.upflow_feedback.domain.FeedbackStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class FeedbackModeratedListener {

    private final FeedbackRepository feedbackRepository;

    @Transactional
    @KafkaListener(topics = "${app.kafka.topics.feedback-moderated}", groupId = "feedback-service")
    public void onModerated(FeedbackModeratedEvent event) {
        UUID feedbackId = UUID.fromString(event.getFeedbackId());
        log.info("Received moderated event for feedback {}: flagged={}, severity={}, pii_detected={}",
                feedbackId,
                event.getFlagged(),
                event.getSeverity(),
                event.getPiiDetected());

        FeedbackStatus status = event.getFlagged() ? FeedbackStatus.FLAGGED : FeedbackStatus.PUBLISHED;
        feedbackRepository.updateModeration(feedbackId, event.getModeratedContent(), status);

        log.info("Updated feedback {} with status {} and moderated content", feedbackId, status);
    }

    @Transactional
    @KafkaListener(topics = "${app.kafka.topics.feedback-flagged}", groupId = "feedback-service")
    public void onFlagged(FeedbackFlaggedEvent event) {
        UUID feedbackId = UUID.fromString(event.getFeedbackId());
        log.warn("Received flagged event for feedback {}: reason={}, severity={}",
                feedbackId,
                event.getReason(),
                event.getSeverity());

        feedbackRepository.updateModeration(feedbackId, null, FeedbackStatus.FLAGGED);

        log.info("Marked feedback {} as FLAGGED", feedbackId);
    }
}
