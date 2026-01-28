package com.abrxu.upflow_feedback.application.mapper;

import com.abrxu.upflow_feedback.application.dto.request.CreateDepartmentRequest;
import com.abrxu.upflow_feedback.application.dto.response.DepartmentResponse;
import com.abrxu.upflow_feedback.domain.Department;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface DepartmentMapper {

    DepartmentResponse toResponse(Department department);

    default Department toDomain(CreateDepartmentRequest request) {
        return Department.create(request.name(), request.description());
    }

}