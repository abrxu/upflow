package com.abrxu.upflow_feedback.infra.persistence;

import com.abrxu.upflow_feedback.infra.FeedbackEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataFeedbackRepository extends JpaRepository<FeedbackEntity, UUID> {

    Page<FeedbackEntity> findByDepartmentId(UUID departmentId, Pageable pageable);

    boolean existsByDepartmentId(UUID departmentId);

}