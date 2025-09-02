package com.abrxu.upflow.department.mappers;

import com.abrxu.upflow.department.domain.Department;
import com.abrxu.upflow.department.dtos.DepartmentCreationDTO;
import com.abrxu.upflow.department.dtos.DepartmentEditDTO;
import com.abrxu.upflow.department.dtos.DepartmentResponseDTO;
import com.abrxu.upflow.user.domain.UserDepartment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    DepartmentResponseDTO departmentToDTO(Department department);

    DepartmentResponseDTO departmentAndAssociationsToDTO(Department department, List<UserDepartment> departments);

    Department dtoToDepartment(DepartmentCreationDTO dto);

    Department editFromDTO(DepartmentEditDTO dto, @MappingTarget Department department);

}
