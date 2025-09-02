package com.abrxu.upflow.user.mappers;

import com.abrxu.upflow.user.domain.UserDepartment;
import com.abrxu.upflow.user.dtos.UserDepartmentResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserDepartmentMapper {

    @Mapping(source = "id.userId", target = "userId")
    @Mapping(source = "id.departmentId", target = "departmentId")
    @Mapping(source = "department.name", target = "departmentName")
    @Mapping(source = "user.credentials.username", target = "username")
    @Mapping(source = "role", target = "role")
    UserDepartmentResponseDTO toDTO(UserDepartment userDepartment);

}
