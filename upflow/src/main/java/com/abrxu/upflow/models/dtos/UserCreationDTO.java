package com.abrxu.upflow.models.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserCreationDTO {

        @Size(max = 80, message = "Your name can't have more than 80 letters.")
        @NotNull(message = "You must fill the name section.")
        @NotBlank(message = "You must fill the name section.")
        private String name;

        @Size(max = 80, message = "Your last name can't have more than 80 letters.")
        @NotNull(message = "You must fill the last name section.")
        @NotBlank(message = "You must fill the last name section.")
        private String lastName;

        @Email(message = "Please provide a valid email.")
        @NotNull(message = "You must fill the email section.")
        @NotBlank(message = "You must fill the email section.")
        private String email;
}
