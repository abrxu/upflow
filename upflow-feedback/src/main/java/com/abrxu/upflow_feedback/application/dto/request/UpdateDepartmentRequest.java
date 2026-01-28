package com.abrxu.upflow_feedback.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateDepartmentRequest(
        @NotBlank String name,
        String description
) {
}