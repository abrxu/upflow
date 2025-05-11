package com.abrxu.upflow.models.department.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record DepartmentCreationDTO(

        @NotNull(message = "You must fill the name section.")
        @NotBlank(message = "You must fill the name section.")
        @Size(min = 3, max = 80, message = "Your department name should have between 3 and 80 letters.")
        String name,

        @NotNull(message = "You must fill the description section.")
        @NotBlank(message = "You must fill the description section.")
        @Size(min = 5, max = 255, message = "Your description should have between 5 and 255 letters.")
        String description,

        @NotNull(message = "You must fill the manager section.")
        Long managerId,

        List<Long> usersIds

) {}
