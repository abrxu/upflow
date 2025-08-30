package com.abrxu.upflow.department.domain;

import com.abrxu.upflow.feedback.domain.Feedback;
import com.abrxu.upflow.user.domain.User;
import com.abrxu.upflow.user.domain.UserDepartment;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "tbl_department")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_department", nullable = false)
    private Long id;

    @Column(name = "txt_name", length = 50, nullable = false)
    private String name;

    @Column(name = "txt_description", nullable = false)
    private String description;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserDepartment> userAssociations = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "department")
    private Set<Feedback> feedbacks = new HashSet<>();

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Department)) return false;
        Department that = (Department) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
