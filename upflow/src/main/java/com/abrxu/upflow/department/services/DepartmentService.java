package com.abrxu.upflow.department.services;

import com.abrxu.upflow.department.dtos.DepartmentCreationDTO;
import com.abrxu.upflow.department.dtos.DepartmentEditDTO;
import com.abrxu.upflow.department.dtos.DepartmentResponseDTO;
import com.abrxu.upflow.department.mappers.DepartmentMapper;
import com.abrxu.upflow.department.repositories.DepartmentRepository;
import com.abrxu.upflow.exceptions.ErrorCode;
import com.abrxu.upflow.exceptions.ErrorCodeException;
import com.abrxu.upflow.user.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@Service
public class DepartmentService {

    private final UserRepository userRepository;
    private final DepartmentMapper departmentMapper;
    private final DepartmentRepository departmentRepository;

    public DepartmentService(UserRepository userRepository, DepartmentMapper departmentMapper, DepartmentRepository departmentRepository) {
        this.userRepository = userRepository;
        this.departmentMapper = departmentMapper;
        this.departmentRepository = departmentRepository;
    }

    @Transactional
    public DepartmentResponseDTO createDepartment(DepartmentCreationDTO dto) {
        var manager = userRepository.findById(dto.managerId())
                .orElseThrow(() -> new ErrorCodeException(ErrorCode.USER_NOT_FOUND));

        var users = new HashSet<>(userRepository.getUsersByIds(dto.usersIds()));

        var department = departmentMapper.dtoToDepartment(dto);

        department.setUsers(users);
        department.setManager(manager);

        departmentRepository.save(department);

        users.add(manager);
        for (var user : users) user.setDepartment(department);
        userRepository.saveAll(users);

        return departmentMapper.departmentToDTO(department);
    }

    @Transactional
    public DepartmentResponseDTO editDepartment(DepartmentEditDTO dto, Long departmentId) {
        var department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ErrorCodeException(ErrorCode.DEPARTMENT_NOT_FOUND));

        var manager = userRepository.findById(dto.managerId())
                .orElseThrow(() -> new ErrorCodeException(ErrorCode.USER_NOT_FOUND));

        var users = new HashSet<>(userRepository.getUsersByIds(dto.usersIds()));

        departmentMapper.editFromDTO(dto, department);

        department.setUsers(users);
        department.setManager(manager);

        departmentRepository.save(department);

        users.add(manager);
        for (var user : users) user.setDepartment(department);
        userRepository.saveAll(users);

        return departmentMapper.departmentToDTO(department);
    }

    public DepartmentResponseDTO getDepartment(Long departmentId) {
        return departmentMapper.departmentToDTO(departmentRepository.findByIdAndFetchRelations(departmentId)
                .orElseThrow(() -> new ErrorCodeException(ErrorCode.DEPARTMENT_NOT_FOUND)));
    }

}
