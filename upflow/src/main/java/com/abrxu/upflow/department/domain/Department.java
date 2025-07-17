package com.abrxu.upflow.department.domain;

import com.abrxu.upflow.feedback.domain.Feedback;
import com.abrxu.upflow.user.domain.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "tbl_department")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_department", nullable = false)
    private Long id;

    @Column(name = "txt_name", length = 50, nullable = false)
    private String name;

    @Column(name = "txt_description", nullable = false)
    private String description;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_manager", nullable = false)
    private User manager;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "department")
    @JsonManagedReference
    private List<User> users = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "department")
    private List<Feedback> feedbacks = new ArrayList<>();

    @Column(name = "dt_created_at", nullable = false, updatable = false)
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
