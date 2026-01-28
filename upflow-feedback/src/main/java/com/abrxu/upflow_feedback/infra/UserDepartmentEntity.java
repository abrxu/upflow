package com.abrxu.upflow_feedback.infra;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "tbl_user_department")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDepartmentEntity {

    @EmbeddedId
    private UserDepartmentId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("departmentId")
    @JoinColumn(name = "department_id")
    private DepartmentEntity department;

    public UserDepartmentEntity(UserEntity user, DepartmentEntity department) {
        this.id = new UserDepartmentId(user.getId(), department.getId());
        this.user = user;
        this.department = department;
    }

    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserDepartmentId implements java.io.Serializable {
        private UUID userId;
        private UUID departmentId;
    }
}