package com.abrxu.upflow.user.dtos;

import com.abrxu.upflow.user.domain.DepartmentRole;

public record UserDepartmentUserCreationDTO(

        Long departmentId,
        DepartmentRole role


) {
}
