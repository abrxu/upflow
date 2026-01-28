package com.abrxu.upflow_feedback.infra;

import com.abrxu.upflow_feedback.domain.Department;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "tbl_department")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    public Department toDomain() {
        return new Department(id, name, description);
    }

    public static DepartmentEntity fromDomain(Department department) {
        return new DepartmentEntity(
                department.id(),
                department.name(),
                department.description()
        );
    }
}