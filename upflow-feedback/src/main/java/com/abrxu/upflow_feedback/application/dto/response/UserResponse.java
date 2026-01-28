package com.abrxu.upflow_feedback.application.dto.response;

import java.util.Set;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String email,
        UUID roleId,
        boolean active,
        Set<UUID> departmentIds
) {
}