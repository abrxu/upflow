package com.abrxu.upflow.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "tbl_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 80, message = "Your name can't have more than 80 letters.")
    @NotNull(message = "You must fill the name section.")
    @NotBlank(message = "You must fill the name section.")
    @Column(name = "txt_name")
    private String name;

    @Size(max = 80, message = "Your last name can't have more than 80 letters.")
    @NotNull(message = "You must fill the last name section.")
    @NotBlank(message = "You must fill the last name section.")
    @Column(name = "txt_last_name")
    private String lastName;

    @Email(message = "Please provide a valid email.")
    @NotNull(message = "You must fill the email section.")
    @NotBlank(message = "You must fill the email section.")
    @Column(name = "txt_email")
    private String email;

    @NotNull(message = "It occurred an error during the user creation. Please try again.")
    private LocalDateTime createdAt;

    private String password;

    @PrePersist
    private void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    private LocalDateTime updatedAt;

    @PreUpdate
    private void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
