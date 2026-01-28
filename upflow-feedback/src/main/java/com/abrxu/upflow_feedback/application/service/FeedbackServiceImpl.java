package com.abrxu.upflow_feedback.application.service;

import com.abrxu.upflow_feedback.application.dto.request.CreateFeedbackRequest;
import com.abrxu.upflow_feedback.application.dto.response.FeedbackResponse;
import com.abrxu.upflow_feedback.application.exception.ResourceNotFoundException;
import com.abrxu.upflow_feedback.application.mapper.FeedbackMapper;
import com.abrxu.upflow_feedback.application.port.in.FeedbackService;
import com.abrxu.upflow_feedback.application.port.out.DepartmentRepository;
import com.abrxu.upflow_feedback.application.port.out.FeedbackRepository;
import com.abrxu.upflow_feedback.domain.Feedback;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final DepartmentRepository departmentRepository;
    private final FeedbackMapper feedbackMapper;

    @Override
    public FeedbackResponse create(CreateFeedbackRequest request) {
        if (!departmentRepository.existsById(request.departmentId())) {
            throw new ResourceNotFoundException("Department not found with id: " + request.departmentId());
        }
        Feedback feedback = feedbackMapper.toDomain(request);
        return feedbackMapper.toResponse(feedbackRepository.save(feedback));
    }

    @Override
    @Transactional(readOnly = true)
    public FeedbackResponse getById(UUID id) {
        return feedbackRepository.findById(id)
                .map(feedbackMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FeedbackResponse> getAll(Pageable pageable) {
        return feedbackRepository.findAll(pageable).map(feedbackMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FeedbackResponse> getByDepartment(UUID departmentId, Pageable pageable) {
        if (!departmentRepository.existsById(departmentId)) {
            throw new ResourceNotFoundException("Department not found with id: " + departmentId);
        }
        return feedbackRepository.findByDepartmentId(departmentId, pageable).map(feedbackMapper::toResponse);
    }

    @Override
    public void delete(UUID id) {
        if (feedbackRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Feedback not found with id: " + id);
        }
        feedbackRepository.deleteById(id);
    }

}