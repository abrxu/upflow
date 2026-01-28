package com.abrxu.upflow_feedback.infra;

import com.abrxu.upflow_feedback.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "tbl_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "role_id", nullable = false)
    private UUID roleId;

    @Column(nullable = false)
    private boolean active;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserDepartmentEntity> userDepartments = new HashSet<>();

    public User toDomain() {
        Set<UUID> departmentIds = userDepartments.stream()
                .map(ud -> ud.getDepartment().getId())
                .collect(Collectors.toSet());
        return new User(id, name, email, roleId, active, departmentIds);
    }

    public static UserEntity fromDomain(User user, Set<DepartmentEntity> departmentEntities) {
        UserEntity entity = new UserEntity();
        entity.setId(user.id());
        entity.setName(user.name());
        entity.setEmail(user.email());
        entity.setRoleId(user.roleId());
        entity.setActive(user.active());

        Set<UserDepartmentEntity> userDepts = departmentEntities.stream()
                .map(dept -> new UserDepartmentEntity(entity, dept))
                .collect(Collectors.toSet());
        entity.setUserDepartments(userDepts);

        return entity;
    }
}