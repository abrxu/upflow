package com.abrxu.upflow_feedback.infra.persistence;

import com.abrxu.upflow_feedback.infra.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataUserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByRoleId(UUID roleId);

    @Query("SELECT DISTINCT u FROM UserEntity u JOIN u.userDepartments ud WHERE ud.department.id = :departmentId")
    List<UserEntity> findByDepartmentId(@Param("departmentId") UUID departmentId);

}