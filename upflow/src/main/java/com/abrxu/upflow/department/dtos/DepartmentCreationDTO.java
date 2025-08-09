package com.abrxu.upflow.department.dtos;

import java.util.List;

public record DepartmentCreationDTO(

        String name,

        String description,

        Long managerId,

        List<Long> usersIds

) {
}
