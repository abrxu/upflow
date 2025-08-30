package com.abrxu.upflow.user.domain;

import com.abrxu.upflow.department.domain.Department;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "tbl_user_department")
public class UserDepartment {

    @Embeddable
    @Getter
    @Setter
    public static class UserDepartmentId implements Serializable {
        @Column(name = "id_user")
        private Long userId;

        @Column(name = "id_department")
        private Long departmentId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            UserDepartmentId that = (UserDepartmentId) o;
            return Objects.equals(userId, that.userId) && Objects.equals(departmentId, that.departmentId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, departmentId);
        }
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
