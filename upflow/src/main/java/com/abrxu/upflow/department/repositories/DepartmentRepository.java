package com.abrxu.upflow.department.repositories;

import com.abrxu.upflow.department.domain.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    @Query("""
           SELECT d.id
           FROM Department d
           WHERE (:search IS NULL OR
           :search = '' OR
           UPPER(d.description) LIKE UPPER(CONCAT('%', :search, '%')) OR
           UPPER(d.name) LIKE UPPER(CONCAT('%', :search, '%'))
           )
           """)
    Page<Long> getPaginatedDepartmentIds(String search, Pageable pageable);

    @Query("""
           SELECT DISTINCT d
           FROM Department d
           LEFT JOIN FETCH d.users
           LEFT JOIN FETCH d.manager
           WHERE d.id IN :ids
           """)
    List<Department> findDepartmentsWithUsersByIds(@Param("ids") List<Long> ids);

}
