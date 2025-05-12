package com.abrxu.upflow.user.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "tbl_user_credentials")
public class UserCredentials {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Size(min = 3, max = 24, message = "Your username must have more than 3 letters and less than 24.")
    @NotNull(message = "You must fill the username section.")
    @NotBlank(message = "You must fill the username section.")
    @Column(name = "txt_username")
    private String username;

    @Email(message = "Please provide a valid email.")
    @NotNull(message = "You must fill the email section.")
    @NotBlank(message = "You must fill the email section.")
    @Column(name = "txt_email")
    private String email;

    @Size(min = 8, max = 24, message = "Your password must have more than 8 letters and less than 24.")
    @Column(name = "txt_password")
    private String password;

    @Column(name = "dt_last_updated")
    private LocalDateTime lastUpdated;

    @OneToOne(mappedBy = "credentials")
    private User user;

    @PrePersist
    private void onCreate() {
        this.lastUpdated = LocalDateTime.now();
    }

    @PreUpdate
    private void onUpdate() {
        this.lastUpdated = LocalDateTime.now();
    }


}
