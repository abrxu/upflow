package com.abrxu.upflow_feedback.application.mapper;

import com.abrxu.upflow_feedback.application.dto.request.CreateFeedbackRequest;
import com.abrxu.upflow_feedback.application.dto.response.FeedbackResponse;
import com.abrxu.upflow_feedback.domain.Feedback;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface FeedbackMapper {

    FeedbackResponse toResponse(Feedback feedback);

    default Feedback toDomain(CreateFeedbackRequest request) {
        return Feedback.create(request.message(), request.rating(), request.departmentId());
    }

}