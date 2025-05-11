package com.abrxu.upflow.models.user.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateDTO {

    @Size(max = 80, message = "Your name can't have more than 80 letters.")
    @NotNull(message = "You must fill the name section.")
    @NotBlank(message = "You must fill the name section.")
    private String name;

    @Size(max = 80, message = "Your last name can't have more than 80 letters.")
    @NotNull(message = "You must fill the last name section.")
    @NotBlank(message = "You must fill the last name section.")
    private String lastName;

}
