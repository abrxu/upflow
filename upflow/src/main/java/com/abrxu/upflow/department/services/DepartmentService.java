package com.abrxu.upflow.department.services;

import com.abrxu.upflow.department.domain.Department;
import com.abrxu.upflow.department.dtos.DepartmentCreationDTO;
import com.abrxu.upflow.department.dtos.DepartmentEditDTO;
import com.abrxu.upflow.department.dtos.DepartmentResponseDTO;
import com.abrxu.upflow.department.mappers.DepartmentMapper;
import com.abrxu.upflow.department.repositories.DepartmentRepository;
import com.abrxu.upflow.exceptions.ErrorCode;
import com.abrxu.upflow.exceptions.ErrorCodeException;
import com.abrxu.upflow.user.domain.User;
import com.abrxu.upflow.user.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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

        validateManager(dto.managerId(), dto.usersIds());

        var manager = userRepository.findById(dto.managerId())
                .orElseThrow(() -> new ErrorCodeException(ErrorCode.USER_NOT_FOUND));

        var users = new HashSet<>(userRepository.getUsersByIds(dto.usersIds()));

        var department = departmentMapper.dtoToDepartment(dto);

        department.setUsers(users);
        department.setManager(manager);

        departmentRepository.save(department);

        setDepartmentToUsers(users, manager, department);

        return departmentMapper.departmentToDTO(department);
    }

    @Transactional
    public DepartmentResponseDTO editDepartment(DepartmentEditDTO dto, Long departmentId) {

        validateManager(dto.managerId(), dto.usersIds());

        var department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ErrorCodeException(ErrorCode.DEPARTMENT_NOT_FOUND));

        var manager = userRepository.findById(dto.managerId())
                .orElseThrow(() -> new ErrorCodeException(ErrorCode.USER_NOT_FOUND));

        var users = new HashSet<>(userRepository.getUsersByIds(dto.usersIds()));

        departmentMapper.editFromDTO(dto, department);

        department.setUsers(users);
        department.setManager(manager);

        departmentRepository.save(department);

        setDepartmentToUsers(users, manager, department);

        return departmentMapper.departmentToDTO(department);
    }

    public DepartmentResponseDTO getDepartment(Long departmentId) {
        return departmentMapper.departmentToDTO(departmentRepository.findByIdAndFetchRelations(departmentId)
                .orElseThrow(() -> new ErrorCodeException(ErrorCode.DEPARTMENT_NOT_FOUND)));
    }

    public Page<DepartmentResponseDTO> getPaginatedDepartments(String search, Pageable pageable) {

        Page<Long> departmentIds = departmentRepository.getPaginatedDepartmentIds(search, pageable);

        List<Department> departments = departmentRepository.findDepartmentsWithUsersByIds(departmentIds.getContent());

        Map<Long, Department> departmentMap = departments.stream()
                .collect(Collectors.toMap(Department::getId, Function.identity()));

        List<DepartmentResponseDTO> dtos = departmentIds.getContent().stream()
                .map(departmentMap::get)
                .map(departmentMapper::departmentToDTO)
                .toList();

        return new PageImpl<>(dtos, pageable, departmentIds.getTotalElements());
    }

    public void validateManager(Long managerId, List<Long> usersIds) {
        if (usersIds
                .stream()
                .anyMatch(u -> u.equals(managerId))) {
            throw new ErrorCodeException(ErrorCode.USER_MANAGER_CANT_BE_AN_EMPLOYEE);
        }
    }

    @Transactional
    public void setDepartmentToUsers(Set<User> users, User manager, Department department) {
        users.add(manager);
        for (var user : users) user.setDepartment(department);
        userRepository.saveAll(users);
    }

}
