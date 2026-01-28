package com.abrxu.upflow_feedback.application.port.in;

import com.abrxu.upflow_feedback.application.dto.request.CreateRoleRequest;
import com.abrxu.upflow_feedback.application.dto.request.UpdateRoleRequest;
import com.abrxu.upflow_feedback.application.dto.response.RoleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface RoleService {

    RoleResponse create(CreateRoleRequest request);

    RoleResponse getById(UUID id);

    Page<RoleResponse> getAll(Pageable pageable);

    RoleResponse update(UUID id, UpdateRoleRequest request);

    void delete(UUID id);

}