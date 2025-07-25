package com.abrxu.upflow.user.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserEditDTO {

    @Size(max = 80, message = "Your name can't have more than 80 letters.")
    private String name;

    @Size(max = 80, message = "Your last name can't have more than 80 letters.")
    private String lastName;

    @Email
    private String email;

}
