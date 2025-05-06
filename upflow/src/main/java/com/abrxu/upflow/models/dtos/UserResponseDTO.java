package com.abrxu.upflow.models.dtos;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponseDTO {

        private Long id;
        private String name;
        private String lastName;
        private String email;
        private LocalDateTime createdAt;

}
