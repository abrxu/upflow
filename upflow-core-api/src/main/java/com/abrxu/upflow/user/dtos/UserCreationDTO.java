package com.abrxu.upflow.user.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserCreationDTO(

        @Size(min = 3, max = 24, message = "Your username must have more than 3 letters and less than 24.")
        @NotNull(message = "You must fill the username section.")
        @NotBlank(message = "You must fill the username section.")
        String username,

        @Size(max = 80, message = "Your name can't have more than 80 letters.")
        @NotNull(message = "You must fill the name section.")
        @NotBlank(message = "You must fill the name section.")
        String name,

        @Size(max = 80, message = "Your last name can't have more than 80 letters.")
        @NotNull(message = "You must fill the last name section.")
        @NotBlank(message = "You must fill the last name section.")
        String lastName,

        @Email(message = "Please provide a valid email.")
        @NotNull(message = "You must fill the email section.")
        @NotBlank(message = "You must fill the email section.")
        String email,

        @Size(min = 8, max = 24, message = "Your password must have more than 8 letters and less than 24.")
        @NotNull(message = "You must fill the password section.")
        @NotBlank(message = "You must fill the password section.")
        String password,

        Long departmentId

)
{}
