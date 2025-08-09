package com.abrxu.upflow.department.dtos;

import com.abrxu.upflow.user.domain.User;

import java.time.LocalDateTime;
import java.util.Set;

public record DepartmentResponseDTO(

        Long id,

        String name,

        String description,

        User manager,

        Set<User> users,

        LocalDateTime createdAt,

        LocalDateTime updatedAt

) {}
