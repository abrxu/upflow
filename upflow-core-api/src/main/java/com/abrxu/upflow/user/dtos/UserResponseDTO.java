package com.abrxu.upflow.user.dtos;

import java.time.LocalDateTime;
import java.util.Set;

public record UserResponseDTO(

        Long id,
        String username,
        String name,
        String lastName,
        String email,
        LocalDateTime createdAt,
        Set<UserDepartmentResponseDTO> associations
)
{}
