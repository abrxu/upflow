package com.abrxu.upflow.user.domain;

import com.abrxu.upflow.department.domain.Department;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "tbl_user_department")
public class UserDepartment {

    public UserDepartment(User user, Department department, DepartmentRole role) {
        this.user = user;
        this.department = department;
        this.role = role;
        this.id = new UserDepartmentId(user.getId(), department.getId());
    }

    @EmbeddedId
    private UserDepartmentId id = new UserDepartmentId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "id_user")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("departmentId")
    @JoinColumn(name = "id_department")
    private Department department;

    @Enumerated(EnumType.STRING)
    @Column(name = "txt_role", nullable = false)
    private DepartmentRole role;

    @Column(name = "dt_joined_at", nullable = false, updatable = false)
    private LocalDateTime joinedAt;

    @PrePersist
    private void onCreate() {
        this.joinedAt = LocalDateTime.now();
    }

}
