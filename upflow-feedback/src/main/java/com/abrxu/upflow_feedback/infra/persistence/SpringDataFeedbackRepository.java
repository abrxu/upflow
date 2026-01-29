package com.abrxu.upflow_feedback.infra.persistence;

import com.abrxu.upflow_feedback.domain.FeedbackStatus;
import com.abrxu.upflow_feedback.infra.FeedbackEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface SpringDataFeedbackRepository extends JpaRepository<FeedbackEntity, UUID> {

    Page<FeedbackEntity> findByDepartmentId(UUID departmentId, Pageable pageable);

    boolean existsByDepartmentId(UUID departmentId);

    @Modifying
    @Query("UPDATE FeedbackEntity f SET f.moderatedContent = :content, f.status = :status WHERE f.id = :id")
    void updateModeration(@Param("id") UUID id, @Param("content") String content, @Param("status") FeedbackStatus status);

}