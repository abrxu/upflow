package com.abrxu.upflow.user.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_user_credentials")
public class UserCredentials {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_credentials")
    private Long id;

    @Size(min = 3, max = 24, message = "Your username must have more than 3 letters and less than 24.")
    @NotNull(message = "You must fill the username section.")
    @NotBlank(message = "You must fill the username section.")
    @Column(name = "txt_username")
    private String username;

    @Email(message = "Please provide a valid email.")
    @NotNull(message = "You must fill the email section.")
    @NotBlank(message = "You must fill the email section.")
    @Column(name = "txt_email", length = 320)
    private String email;

    @Size(min = 8, max = 24, message = "Your password must have more than 8 letters and less than 24.")
    @Column(name = "txt_password")
    private String password;

    @Column(name = "dt_updated_at")
    private LocalDateTime updatedAt;

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

}
