package com.abrxu.upflow_feedback.infra.kafka;

import com.abrxu.upflow.events.FeedbackSubmittedEvent;
import com.abrxu.upflow_feedback.domain.Feedback;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class FeedbackEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.kafka.topics.feedback-submitted}")
    private String feedbackSubmittedTopic;

    public void publishSubmitted(Feedback feedback, String tenantId) {
        var event = FeedbackSubmittedEvent.newBuilder()
                .setFeedbackId(feedback.id().toString())
                .setContent(feedback.message())
                .setTenantId(tenantId)
                .setDepartmentId(feedback.departmentId().toString())
                .setSubmittedAt(feedback.createdAt())
                .setMetadata(Map.of("rating", String.valueOf(feedback.rating())))
                .build();

        kafkaTemplate.send(feedbackSubmittedTopic, feedback.id().toString(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish feedback.submitted event for {}: {}", feedback.id(), ex.getMessage());
                    } else {
                        log.info("Published feedback.submitted event for {} to partition {} offset {}",
                                feedback.id(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });
    }
}
