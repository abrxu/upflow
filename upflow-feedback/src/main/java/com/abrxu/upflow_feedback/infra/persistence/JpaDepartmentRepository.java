package com.abrxu.upflow_feedback.infra.persistence;

import com.abrxu.upflow_feedback.application.port.out.DepartmentRepository;
import com.abrxu.upflow_feedback.domain.Department;
import com.abrxu.upflow_feedback.infra.DepartmentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaDepartmentRepository implements DepartmentRepository {

    private final SpringDataDepartmentRepository jpaRepository;

    @Override
    public Department save(Department department) {
        DepartmentEntity entity = DepartmentEntity.fromDomain(department);
        return jpaRepository.save(entity).toDomain();
    }

    @Override
    public Optional<Department> findById(UUID id) {
        return jpaRepository.findById(id).map(DepartmentEntity::toDomain);
    }

    @Override
    public Page<Department> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(DepartmentEntity::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaRepository.existsById(id);
    }

}