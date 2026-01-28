package com.abrxu.upflow_feedback.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateDepartmentRequest(
        @NotBlank String name,
        String description
) {
}