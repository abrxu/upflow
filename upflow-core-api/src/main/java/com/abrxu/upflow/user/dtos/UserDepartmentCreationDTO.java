package com.abrxu.upflow.user.dtos;

import com.abrxu.upflow.user.domain.DepartmentRole;

public record UserDepartmentCreationDTO(

        Long departmentId,
        Long userId,
        DepartmentRole role

) {
}
