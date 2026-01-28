package com.abrxu.upflow_feedback.application.mapper;

import com.abrxu.upflow_feedback.application.dto.request.CreateUserRequest;
import com.abrxu.upflow_feedback.application.dto.response.UserResponse;
import com.abrxu.upflow_feedback.domain.User;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface UserMapper {

    UserResponse toResponse(User user);

    List<UserResponse> toResponseList(List<User> users);

    default User toDomain(CreateUserRequest request) {
        return User.create(request.name(), request.email(), request.roleId(), request.departmentIds());
    }

}