package com.abrxu.upflow.user.dtos;

import java.util.Set;

public record UserEditDTO(

        String name,

        String lastName,

        String email,

        Set<UserDepartmentCreationDTO> associations

) {}
