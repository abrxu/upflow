package com.abrxu.upflow_feedback.domain;

import java.util.Set;
import java.util.UUID;

public record User(
        UUID id,
        String name,
        String email,
        UUID roleId,
        boolean active,
        Set<UUID> departmentIds
) {
    public User {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name cannot be blank");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("email cannot be blank");
        }
        if (roleId == null) {
            throw new IllegalArgumentException("roleId cannot be null");
        }
        departmentIds = departmentIds != null ? Set.copyOf(departmentIds) : Set.of();
    }

    public static User create(String name, String email, UUID roleId, Set<UUID> departmentIds) {
        return new User(UUID.randomUUID(), name, email, roleId, true, departmentIds);
    }
}