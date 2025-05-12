package com.abrxu.upflow.user.domain;

import com.abrxu.upflow.department.domain.Department;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_department")
    @JsonBackReference
    private Department department;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_credentials")
    private UserCredentials credentials;

    @Column(name = "dt_last_feedback")
    private LocalDateTime lastFeedback;

    @NotNull(message = "It occurred an error during the user creation. Please try again.")
    @Column(name = "dt_created_at")
    private LocalDateTime createdAt;

    @PrePersist
    private void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @Column(name = "dt_updated_at")
    private LocalDateTime updatedAt;

    @PreUpdate
    private void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
