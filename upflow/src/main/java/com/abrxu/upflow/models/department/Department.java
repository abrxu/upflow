package com.abrxu.upflow.models.department;

import com.abrxu.upflow.models.User;
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_manager")
    private User manager;

    @OneToMany(mappedBy = "department")
    private List<User> users;

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
