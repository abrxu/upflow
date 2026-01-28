package com.abrxu.upflow_feedback.application.mapper;

import com.abrxu.upflow_feedback.application.dto.request.CreateRoleRequest;
import com.abrxu.upflow_feedback.application.dto.response.RoleResponse;
import com.abrxu.upflow_feedback.domain.Role;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface RoleMapper {

    RoleResponse toResponse(Role role);

    default Role toDomain(CreateRoleRequest request) {
        return Role.create(request.name());
    }

}