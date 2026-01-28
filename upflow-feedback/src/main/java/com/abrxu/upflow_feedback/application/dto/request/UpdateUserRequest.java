package com.abrxu.upflow_feedback.application.dto.request;

import java.util.Set;
import java.util.UUID;

public record UpdateUserRequest(
        String name,
        UUID roleId,
        Boolean active,
        Set<UUID> departmentIds
) {
}