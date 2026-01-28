package com.abrxu.upflow_feedback.application.port.in;

import com.abrxu.upflow_feedback.application.dto.request.CreateFeedbackRequest;
import com.abrxu.upflow_feedback.application.dto.response.FeedbackResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface FeedbackService {

    FeedbackResponse create(CreateFeedbackRequest request);

    FeedbackResponse getById(UUID id);

    Page<FeedbackResponse> getAll(Pageable pageable);

    Page<FeedbackResponse> getByDepartment(UUID departmentId, Pageable pageable);

    void delete(UUID id);

}