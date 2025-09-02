package com.abrxu.upflow.user.dtos;

import com.abrxu.upflow.user.domain.DepartmentRole;

public record UserDepartmentResponseDTO(

        Long userId,
        String username,
        Long departmentId,
        String departmentName,
        DepartmentRole role

) {
}
