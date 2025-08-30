package com.abrxu.upflow.department.mappers;

import com.abrxu.upflow.department.domain.Department;
import com.abrxu.upflow.department.dtos.DepartmentCreationDTO;
import com.abrxu.upflow.department.dtos.DepartmentEditDTO;
import com.abrxu.upflow.department.dtos.DepartmentResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    DepartmentResponseDTO departmentToDTO(Department department);

    Department dtoToDepartment(DepartmentCreationDTO dto);

    Department editFromDTO(DepartmentEditDTO dto, @MappingTarget Department department);

}
