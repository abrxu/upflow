package com.abrxu.upflow_feedback.application.port.out;

import com.abrxu.upflow_feedback.domain.Feedback;
import com.abrxu.upflow_feedback.domain.FeedbackStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface FeedbackRepository {

    Feedback save(Feedback feedback);

    Optional<Feedback> findById(UUID id);

    Page<Feedback> findAll(Pageable pageable);

    Page<Feedback> findByDepartmentId(UUID departmentId, Pageable pageable);

    void deleteById(UUID id);

    boolean existsByDepartmentId(UUID departmentId);

    void updateModeration(UUID id, String moderatedContent, FeedbackStatus status);

}