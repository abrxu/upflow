package com.abrxu.upflow_feedback.infra.persistence;

import com.abrxu.upflow_feedback.infra.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface SpringDataDepartmentRepository extends JpaRepository<DepartmentEntity, UUID> {

    List<DepartmentEntity> findAllByIdIn(Set<UUID> ids);

}