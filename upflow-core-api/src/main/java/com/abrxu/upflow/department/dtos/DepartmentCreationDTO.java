package com.abrxu.upflow.department.dtos;

import com.abrxu.upflow.user.dtos.UserDepartmentCreationDTO;

import java.util.Set;

public record DepartmentCreationDTO(

        String name,

        String description,

        Set<UserDepartmentCreationDTO> associations

) {
}
