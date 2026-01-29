package com.abrxu.upflow_feedback.infra.persistence;

import com.abrxu.upflow_feedback.application.port.out.FeedbackRepository;
import com.abrxu.upflow_feedback.domain.Feedback;
import com.abrxu.upflow_feedback.domain.FeedbackStatus;
import com.abrxu.upflow_feedback.infra.FeedbackEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaFeedbackRepository implements FeedbackRepository {

    private final SpringDataFeedbackRepository jpaRepository;

    @Override
    public Feedback save(Feedback feedback) {
        FeedbackEntity entity = FeedbackEntity.fromDomain(feedback);
        return jpaRepository.save(entity).toDomain();
    }

    @Override
    public Optional<Feedback> findById(UUID id) {
        return jpaRepository.findById(id).map(FeedbackEntity::toDomain);
    }

    @Override
    public Page<Feedback> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(FeedbackEntity::toDomain);
    }

    @Override
    public Page<Feedback> findByDepartmentId(UUID departmentId, Pageable pageable) {
        return jpaRepository.findByDepartmentId(departmentId, pageable).map(FeedbackEntity::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByDepartmentId(UUID departmentId) {
        return jpaRepository.existsByDepartmentId(departmentId);
    }

    @Override
    public void updateModeration(UUID id, String moderatedContent, FeedbackStatus status) {
        jpaRepository.updateModeration(id, moderatedContent, status);
    }

}