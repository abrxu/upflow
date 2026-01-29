package com.abrxu.upflow_feedback.infra.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicsConfig {

    @Value("${app.kafka.topics.feedback-submitted}")
    private String feedbackSubmittedTopic;

    @Value("${app.kafka.topics.feedback-moderated}")
    private String feedbackModeratedTopic;

    @Value("${app.kafka.topics.feedback-flagged}")
    private String feedbackFlaggedTopic;

    @Bean
    public NewTopic feedbackSubmittedTopic() {
        return TopicBuilder.name(feedbackSubmittedTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic feedbackModeratedTopic() {
        return TopicBuilder.name(feedbackModeratedTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic feedbackFlaggedTopic() {
        return TopicBuilder.name(feedbackFlaggedTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
