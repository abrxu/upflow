package com.abrxu.upflow_feedback.application.port.out;

import com.abrxu.upflow_feedback.domain.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface DepartmentRepository {

    Department save(Department department);

    Optional<Department> findById(UUID id);

    Page<Department> findAll(Pageable pageable);

    void deleteById(UUID id);

    boolean existsById(UUID id);

}