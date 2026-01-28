package com.abrxu.upflow_feedback.infra;

import com.abrxu.upflow_feedback.domain.Role;
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
@Table(name = "tbl_role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleEntity {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    public Role toDomain() {
        return new Role(id, name);
    }

    public static RoleEntity fromDomain(Role role) {
        return new RoleEntity(role.id(), role.name());
    }
}