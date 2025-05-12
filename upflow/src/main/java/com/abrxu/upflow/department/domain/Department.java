package com.abrxu.upflow.department.domain;

import com.abrxu.upflow.feedback.domain.Feedback;
import com.abrxu.upflow.user.domain.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "tbl_department")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "txt_name", length = 50)
    private String name;

    @Column(name = "txt_description")
    private String description;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_manager")
    private User manager;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "department")
    @JsonManagedReference
    private List<User> users;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "department")
    private List<Feedback> feedbacks;

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
