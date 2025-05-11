package com.abrxu.upflow.services;

import com.abrxu.upflow.models.department.Department;
import com.abrxu.upflow.models.department.dtos.DepartmentCreationDTO;
import com.abrxu.upflow.repositories.DepartmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService {

    final DepartmentRepository departmentRepository;
    final UserService userService;

    public DepartmentService(DepartmentRepository departmentRepository, UserService userService) {
        this.departmentRepository = departmentRepository;
        this.userService = userService;
    }

    @Transactional
    public Department createDepartment(DepartmentCreationDTO dto) {
        var department = new Department();
        var users = userService.findUsersByIds(dto.usersIds());
        var manager = userService.findUserById(dto.managerId());
        department.setUsers(users);
        department.setManager(manager);
        BeanUtils.copyProperties(dto, department);

        return department;
    }


    public Department findDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found."));
    }
}
