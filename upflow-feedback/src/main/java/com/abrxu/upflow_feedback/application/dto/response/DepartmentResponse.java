package com.abrxu.upflow_feedback.application.dto.response;

import java.util.UUID;

public record DepartmentResponse(UUID id, String name, String description) {
}