package com.abrxu.upflow.models.user.dtos;

import com.abrxu.upflow.models.department.Department;
import com.abrxu.upflow.models.user.User;

import java.time.LocalDateTime;

public record UserResponseDTO(

        Long id,
        String username,
        String name,
        String lastName,
        String email,
        LocalDateTime createdAt,
        Department department
)
{
        public static UserResponseDTO from(User user) {
                return new UserResponseDTO(
                        user.getId(),
                        user.getCredentials().getUsername(),
                        user.getName(),
                        user.getLastName(),
                        user.getCredentials().getEmail(),
                        user.getCreatedAt(),
                        user.getDepartment()
                );
        }

}
