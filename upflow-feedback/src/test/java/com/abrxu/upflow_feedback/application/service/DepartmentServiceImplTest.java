package com.abrxu.upflow_feedback.application.service;

import com.abrxu.upflow_feedback.application.dto.request.CreateDepartmentRequest;
import com.abrxu.upflow_feedback.application.dto.request.UpdateDepartmentRequest;
import com.abrxu.upflow_feedback.application.dto.response.DepartmentResponse;
import com.abrxu.upflow_feedback.application.exception.ResourceNotFoundException;
import com.abrxu.upflow_feedback.application.mapper.DepartmentMapper;
import com.abrxu.upflow_feedback.application.port.out.DepartmentRepository;
import com.abrxu.upflow_feedback.application.port.out.FeedbackRepository;
import com.abrxu.upflow_feedback.application.port.out.UserRepository;
import com.abrxu.upflow_feedback.domain.Department;
import com.abrxu.upflow_feedback.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DepartmentMapper departmentMapper;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    private UUID departmentId;
    private Department department;
    private DepartmentResponse departmentResponse;

    @BeforeEach
    void setUp() {
        departmentId = UUID.randomUUID();
        department = new Department(departmentId, "Engineering", "Tech department");
        departmentResponse = new DepartmentResponse(departmentId, "Engineering", "Tech department");
    }

    @Test
    void create_shouldCreateDepartment() {
        CreateDepartmentRequest request = new CreateDepartmentRequest("Engineering", "Tech department");
        when(departmentMapper.toDomain(request)).thenReturn(department);
        when(departmentRepository.save(department)).thenReturn(department);
        when(departmentMapper.toResponse(department)).thenReturn(departmentResponse);

        DepartmentResponse result = departmentService.create(request);

        assertNotNull(result);
        assertEquals("Engineering", result.name());
        verify(departmentRepository).save(department);
    }

    @Test
    void getById_shouldReturnDepartment_whenExists() {
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));
        when(departmentMapper.toResponse(department)).thenReturn(departmentResponse);

        DepartmentResponse result = departmentService.getById(departmentId);

        assertNotNull(result);
        assertEquals(departmentId, result.id());
    }

    @Test
    void getById_shouldThrowResourceNotFoundException_whenNotExists() {
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> departmentService.getById(departmentId));
    }

    @Test
    void getAll_shouldReturnPagedDepartments() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Department> page = new PageImpl<>(List.of(department));
        when(departmentRepository.findAll(pageable)).thenReturn(page);
        when(departmentMapper.toResponse(any(Department.class))).thenReturn(departmentResponse);

        Page<DepartmentResponse> result = departmentService.getAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void update_shouldUpdateDepartment_whenExists() {
        UpdateDepartmentRequest request = new UpdateDepartmentRequest("HR", "Human Resources");
        Department updated = new Department(departmentId, "HR", "Human Resources");
        DepartmentResponse updatedResponse = new DepartmentResponse(departmentId, "HR", "Human Resources");

        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));
        when(departmentRepository.save(any(Department.class))).thenReturn(updated);
        when(departmentMapper.toResponse(updated)).thenReturn(updatedResponse);

        DepartmentResponse result = departmentService.update(departmentId, request);

        assertEquals("HR", result.name());
    }

    @Test
    void update_shouldThrowResourceNotFoundException_whenNotExists() {
        UpdateDepartmentRequest request = new UpdateDepartmentRequest("HR", "Human Resources");
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> departmentService.update(departmentId, request));
    }

    @Test
    void delete_shouldDeleteDepartment_whenExistsAndNoConstraints() {
        when(departmentRepository.existsById(departmentId)).thenReturn(true);
        when(feedbackRepository.existsByDepartmentId(departmentId)).thenReturn(false);
        when(userRepository.findByDepartmentId(departmentId)).thenReturn(Collections.emptyList());

        departmentService.delete(departmentId);

        verify(departmentRepository).deleteById(departmentId);
    }

    @Test
    void delete_shouldThrowResourceNotFoundException_whenNotExists() {
        when(departmentRepository.existsById(departmentId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> departmentService.delete(departmentId));
    }

    @Test
    void delete_shouldThrowIllegalArgumentException_whenFeedbackExists() {
        when(departmentRepository.existsById(departmentId)).thenReturn(true);
        when(feedbackRepository.existsByDepartmentId(departmentId)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> departmentService.delete(departmentId));
        verify(departmentRepository, never()).deleteById(any());
    }

    @Test
    void delete_shouldThrowIllegalArgumentException_whenUsersAssigned() {
        when(departmentRepository.existsById(departmentId)).thenReturn(true);
        when(feedbackRepository.existsByDepartmentId(departmentId)).thenReturn(false);
        User user = User.create("Test User", "test@example.com", UUID.randomUUID(), Set.of(departmentId));
        when(userRepository.findByDepartmentId(departmentId)).thenReturn(List.of(user));

        assertThrows(IllegalArgumentException.class, () -> departmentService.delete(departmentId));
        verify(departmentRepository, never()).deleteById(any());
    }
}