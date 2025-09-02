package com.abrxu.upflow.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDepartmentId implements Serializable {

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
