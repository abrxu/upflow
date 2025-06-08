package com.abrxu.upflow.department.services;

import com.abrxu.upflow.department.domain.Department;
import com.abrxu.upflow.department.dtos.DepartmentCreationDTO;
import com.abrxu.upflow.department.repositories.DepartmentRepository;
import com.abrxu.upflow.exceptions.ErrorCode;
import com.abrxu.upflow.exceptions.ErrorCodeException;
import com.abrxu.upflow.user.domain.User;
import com.abrxu.upflow.user.services.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class DepartmentManagementService {

    final UserService userService;
    final DepartmentRepository departmentRepository;

    public DepartmentManagementService(UserService userService, DepartmentRepository departmentRepository) {
        this.userService = userService;
        this.departmentRepository = departmentRepository;
    }

    @Transactional
    public Department createDepartment(DepartmentCreationDTO dto) {

        if (dto.usersIds() != null && dto.usersIds().contains(dto.managerId()))
            throw new ErrorCodeException(ErrorCode.MANAGER_CANT_BE_AN_EMPLOYEE);

        var department = new Department();
        BeanUtils.copyProperties(dto, department, "managerId", "usersIds");

        var manager = userService.findUserById(dto.managerId());
        department.setManager(manager);
        manager.setDepartment(department);

        if (!CollectionUtils.isEmpty(dto.usersIds())) {
            List<User> users = userService.findUsersByIds(dto.usersIds());
            users.forEach(u -> u.setDepartment(department));
            department.setUsers(users);
        }

        return departmentRepository.save(department);
    }

}
