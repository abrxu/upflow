package com.abrxu.upflow_feedback.application.port.in;

import com.abrxu.upflow_feedback.application.dto.request.CreateDepartmentRequest;
import com.abrxu.upflow_feedback.application.dto.request.UpdateDepartmentRequest;
import com.abrxu.upflow_feedback.application.dto.response.DepartmentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface DepartmentService {

    DepartmentResponse create(CreateDepartmentRequest request);

    DepartmentResponse getById(UUID id);

    Page<DepartmentResponse> getAll(Pageable pageable);

    DepartmentResponse update(UUID id, UpdateDepartmentRequest request);

    void delete(UUID id);

}