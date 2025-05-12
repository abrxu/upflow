package com.abrxu.upflow.department.services;

import com.abrxu.upflow.department.domain.Department;
import com.abrxu.upflow.department.dtos.DepartmentCreationDTO;
import com.abrxu.upflow.user.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class DepartmentManagementService {

    final UserService userService;

    public DepartmentManagementService(UserService userService) {
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

}
