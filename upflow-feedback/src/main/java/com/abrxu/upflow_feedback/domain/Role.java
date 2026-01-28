package com.abrxu.upflow_feedback.domain;

import java.util.UUID;

public record Role(
        UUID id,
        String name
) {
    public Role {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name cannot be blank");
        }
    }

    public static Role create(String name) {
        return new Role(UUID.randomUUID(), name);
    }
}