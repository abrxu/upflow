package com.abrxu.upflow.department.services;

import com.abrxu.upflow.department.domain.Department;
import com.abrxu.upflow.department.dtos.DepartmentCreationDTO;
import com.abrxu.upflow.department.repositories.DepartmentRepository;
import com.abrxu.upflow.user.domain.User;
import com.abrxu.upflow.user.services.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DepartmentManagementService {

    final UserService userService;
    final DepartmentRepository departmentRepository;

    public DepartmentManagementService(UserService userService, DepartmentRepository departmentRepository) {
        this.userService = userService;
        this.departmentRepository = departmentRepository;
    }

    // TODO: create a Department response DTO to return the department with users.
    @Transactional
    public Department createDepartment(DepartmentCreationDTO dto) {

        if (dto.usersIds().contains(dto.managerId())) throw new RuntimeException("Manager cannot be also a common user.");

        var department = new Department();
        var manager = userService.findUserById(dto.managerId());
        BeanUtils.copyProperties(dto, department, "managerId", "usersIds");

        department.setManager(manager);
        manager.setDepartment(department);

        List<Long> ids = dto.usersIds();
        if (ids != null && !ids.isEmpty()) {
            List<User> users = userService.findUsersByIds(ids);
            users.forEach(user -> user.setDepartment(department));
            department.setUsers(users);
        }

        return departmentRepository.save(department);
    }

}
