package com.abrxu.upflow_feedback.application.port.in;

import com.abrxu.upflow_feedback.application.dto.request.CreateUserRequest;
import com.abrxu.upflow_feedback.application.dto.request.UpdateUserRequest;
import com.abrxu.upflow_feedback.application.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserResponse create(CreateUserRequest request);

    UserResponse getById(UUID id);

    Page<UserResponse> getAll(Pageable pageable);

    List<UserResponse> getByDepartment(UUID departmentId);

    UserResponse update(UUID id, UpdateUserRequest request);

    void delete(UUID id);

}