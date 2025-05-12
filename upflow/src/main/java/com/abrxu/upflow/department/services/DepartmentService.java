package com.abrxu.upflow.department.services;

import com.abrxu.upflow.department.domain.Department;
import com.abrxu.upflow.department.repositories.DepartmentRepository;
import com.abrxu.upflow.user.services.UserService;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService {

    final DepartmentRepository departmentRepository;
    final UserService userService;

    public DepartmentService(DepartmentRepository departmentRepository, UserService userService) {
        this.departmentRepository = departmentRepository;
        this.userService = userService;
    }

    public Department findDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found."));
    }
}
