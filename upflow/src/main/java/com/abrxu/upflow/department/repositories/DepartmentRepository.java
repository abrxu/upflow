package com.abrxu.upflow.department.repositories;

import com.abrxu.upflow.department.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Query("""
           SELECT d FROM Department d
           LEFT JOIN FETCH d.users
           LEFT JOIN FETCH d.manager
           WHERE d.id = :departmentId
           """)
    Optional<Department> findByIdAndFetchRelations(Long departmentId);

}
