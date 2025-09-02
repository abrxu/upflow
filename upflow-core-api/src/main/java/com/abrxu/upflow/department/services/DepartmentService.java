package com.abrxu.upflow.department.services;

import com.abrxu.upflow.department.domain.Department;
import com.abrxu.upflow.department.dtos.DepartmentCreationDTO;
import com.abrxu.upflow.department.dtos.DepartmentEditDTO;
import com.abrxu.upflow.department.dtos.DepartmentResponseDTO;
import com.abrxu.upflow.department.mappers.DepartmentMapper;
import com.abrxu.upflow.department.repositories.DepartmentRepository;
import com.abrxu.upflow.exceptions.ErrorCode;
import com.abrxu.upflow.exceptions.ErrorCodeException;
import com.abrxu.upflow.user.domain.DepartmentRole;
import com.abrxu.upflow.user.domain.User;
import com.abrxu.upflow.user.domain.UserDepartment;
import com.abrxu.upflow.user.dtos.UserDepartmentCreationDTO;
import com.abrxu.upflow.user.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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

        var department = departmentMapper.dtoToDepartment(dto);

        for (var dep : dto.associations()) {
            User user = userRepository.findById(dep.userId())
                    .orElseThrow(() -> new ErrorCodeException(ErrorCode.USER_NOT_FOUND));

            UserDepartment userDepartment = new UserDepartment(user, department, dep.role() != null ? dep.role() : DepartmentRole.MEMBER);

            department.getUserAssociations().add(userDepartment);
        }

        departmentRepository.save(department);

        return departmentMapper.departmentToDTO(department);
    }

    @Transactional
    public DepartmentResponseDTO editDepartment(DepartmentEditDTO dto, Long departmentId) {
        Department department = departmentRepository.findByIdAndFetchRelations(departmentId)
                .orElseThrow(() -> new ErrorCodeException(ErrorCode.DEPARTMENT_NOT_FOUND));

        department.getUserAssociations().clear();

        for (UserDepartmentCreationDTO dep : dto.associations()) {
            User user = userRepository.findById(dep.userId())
                    .orElseThrow(() -> new ErrorCodeException(ErrorCode.USER_NOT_FOUND));

            UserDepartment userDepartment = new UserDepartment(user, department, dep.role() != null ? dep.role() : DepartmentRole.MEMBER);

            department.getUserAssociations().add(userDepartment);
        }

        Department updatedDepartment = departmentRepository.save(department);

        return departmentMapper.departmentToDTO(updatedDepartment);
    }

    @Transactional
    public void deleteDepartment(Long departmentId) {
       var department = departmentRepository.findByIdAndFetchRelations(departmentId)
               .orElseThrow(() -> new ErrorCodeException(ErrorCode.DEPARTMENT_NOT_FOUND));

       departmentRepository.delete(department);
    }

    public DepartmentResponseDTO getDepartment(Long departmentId) {
        return departmentMapper.departmentToDTO(departmentRepository.findByIdAndFetchRelations(departmentId)
                .orElseThrow(() -> new ErrorCodeException(ErrorCode.DEPARTMENT_NOT_FOUND)));
    }

    public Page<DepartmentResponseDTO> getPaginatedDepartments(String search, Pageable pageable) {

        Page<Department> departmentPage = departmentRepository.findDepartmentsWithPagination(search, pageable);
        List<Department> departmentsOnPage = departmentPage.getContent();

        if (departmentsOnPage.isEmpty()) {
            return Page.empty(pageable);
        }

        List<Long> departmentIds = departmentsOnPage.stream()
                .map(Department::getId)
                .toList();

        List<UserDepartment> associations = departmentRepository.findDepartmentAssociationsForDepartments(departmentIds);

        Map<Long, List<UserDepartment>> associationsByDepartmentId = associations.stream()
                .collect(Collectors.groupingBy(ud -> ud.getDepartment().getId()));

        List<DepartmentResponseDTO> dtos = departmentsOnPage.stream()
                .map(department -> {
                    List<UserDepartment> departmentAssociations = associationsByDepartmentId.getOrDefault(department.getId(),
                            Collections.emptyList());
                    return departmentMapper.departmentAndAssociationsToDTO(department, departmentAssociations);
                }).toList();

        return new PageImpl<>(dtos, pageable, departmentPage.getTotalElements());
    }

}
