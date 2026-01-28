package com.abrxu.upflow_feedback.domain;

import java.util.UUID;

public record Department(
        UUID id,
        String name,
        String description
) {
    public Department {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name cannot be blank");
        }
    }

    public static Department create(String name, String description) {
        return new Department(UUID.randomUUID(), name, description);
    }
}