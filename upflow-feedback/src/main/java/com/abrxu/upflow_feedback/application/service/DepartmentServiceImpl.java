package com.abrxu.upflow_feedback.application.service;

import com.abrxu.upflow_feedback.application.dto.request.CreateDepartmentRequest;
import com.abrxu.upflow_feedback.application.dto.request.UpdateDepartmentRequest;
import com.abrxu.upflow_feedback.application.dto.response.DepartmentResponse;
import com.abrxu.upflow_feedback.application.exception.ResourceNotFoundException;
import com.abrxu.upflow_feedback.application.mapper.DepartmentMapper;
import com.abrxu.upflow_feedback.application.port.in.DepartmentService;
import com.abrxu.upflow_feedback.application.port.out.DepartmentRepository;
import com.abrxu.upflow_feedback.application.port.out.FeedbackRepository;
import com.abrxu.upflow_feedback.application.port.out.UserRepository;
import com.abrxu.upflow_feedback.domain.Department;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    public DepartmentResponse create(CreateDepartmentRequest request) {
        Department department = departmentMapper.toDomain(request);
        return departmentMapper.toResponse(departmentRepository.save(department));
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentResponse getById(UUID id) {
        return departmentRepository.findById(id)
                .map(departmentMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepartmentResponse> getAll(Pageable pageable) {
        return departmentRepository.findAll(pageable).map(departmentMapper::toResponse);
    }

    @Override
    public DepartmentResponse update(UUID id, UpdateDepartmentRequest request) {
        Department existing = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));

        Department updated = new Department(existing.id(), request.name(), request.description());
        return departmentMapper.toResponse(departmentRepository.save(updated));
    }

    @Override
    public void delete(UUID id) {
        if (!departmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Department not found with id: " + id);
        }
        if (feedbackRepository.existsByDepartmentId(id)) {
            throw new IllegalArgumentException("Cannot delete department: feedback exists for it");
        }
        if (!userRepository.findByDepartmentId(id).isEmpty()) {
            throw new IllegalArgumentException("Cannot delete department: users are still assigned to it");
        }
        departmentRepository.deleteById(id);
    }

}