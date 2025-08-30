package com.abrxu.upflow.department.dtos;

import java.util.List;

public record DepartmentEditDTO(

        String name,

        String description,

        Long managerId,

        List<Long> usersIds

) {
}
