package com.abrxu.upflow_feedback.application.service;

import com.abrxu.upflow_feedback.application.dto.request.CreateUserRequest;
import com.abrxu.upflow_feedback.application.dto.request.UpdateUserRequest;
import com.abrxu.upflow_feedback.application.dto.response.UserResponse;
import com.abrxu.upflow_feedback.application.exception.DuplicateResourceException;
import com.abrxu.upflow_feedback.application.exception.ResourceNotFoundException;
import com.abrxu.upflow_feedback.application.mapper.UserMapper;
import com.abrxu.upflow_feedback.application.port.out.DepartmentRepository;
import com.abrxu.upflow_feedback.application.port.out.RoleRepository;
import com.abrxu.upflow_feedback.application.port.out.UserRepository;
import com.abrxu.upflow_feedback.domain.Role;
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

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private UUID userId;
    private UUID roleId;
    private UUID departmentId;
    private User user;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        roleId = UUID.randomUUID();
        departmentId = UUID.randomUUID();
        user = new User(userId, "John Doe", "john@example.com", roleId, true, Set.of(departmentId));
        userResponse = new UserResponse(userId, "John Doe", "john@example.com", roleId, true, Set.of(departmentId));
    }

    @Test
    void create_shouldCreateUser_whenValid() {
        CreateUserRequest request = new CreateUserRequest("John Doe", "john@example.com", roleId, Set.of(departmentId));
        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(new Role(roleId, "ADMIN")));
        when(departmentRepository.existsById(departmentId)).thenReturn(true);
        when(userMapper.toDomain(request)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.create(request);

        assertNotNull(result);
        assertEquals("john@example.com", result.email());
        verify(userRepository).save(user);
    }

    @Test
    void create_shouldThrowDuplicateResourceException_whenEmailExists() {
        CreateUserRequest request = new CreateUserRequest("John Doe", "john@example.com", roleId, Set.of(departmentId));
        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> userService.create(request));
        verify(userRepository, never()).save(any());
    }

    @Test
    void create_shouldThrowResourceNotFoundException_whenRoleNotExists() {
        CreateUserRequest request = new CreateUserRequest("John Doe", "john@example.com", roleId, Set.of(departmentId));
        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.create(request));
    }

    @Test
    void create_shouldThrowResourceNotFoundException_whenDepartmentNotExists() {
        CreateUserRequest request = new CreateUserRequest("John Doe", "john@example.com", roleId, Set.of(departmentId));
        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(new Role(roleId, "ADMIN")));
        when(departmentRepository.existsById(departmentId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> userService.create(request));
    }

    @Test
    void getById_shouldReturnUser_whenExists() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.getById(userId);

        assertNotNull(result);
        assertEquals(userId, result.id());
    }

    @Test
    void getById_shouldThrowResourceNotFoundException_whenNotExists() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getById(userId));
    }

    @Test
    void getAll_shouldReturnPagedUsers() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> page = new PageImpl<>(List.of(user));
        when(userRepository.findAll(pageable)).thenReturn(page);
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        Page<UserResponse> result = userService.getAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getByDepartment_shouldReturnUsers_whenDepartmentExists() {
        when(departmentRepository.existsById(departmentId)).thenReturn(true);
        when(userRepository.findByDepartmentId(departmentId)).thenReturn(List.of(user));
        when(userMapper.toResponseList(List.of(user))).thenReturn(List.of(userResponse));

        List<UserResponse> result = userService.getByDepartment(departmentId);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getByDepartment_shouldThrowResourceNotFoundException_whenDepartmentNotExists() {
        when(departmentRepository.existsById(departmentId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> userService.getByDepartment(departmentId));
    }

    @Test
    void update_shouldUpdateUser_whenValid() {
        UpdateUserRequest request = new UpdateUserRequest("Jane Doe", null, null, null);
        User updated = new User(userId, "Jane Doe", "john@example.com", roleId, true, Set.of(departmentId));
        UserResponse updatedResponse = new UserResponse(userId, "Jane Doe", "john@example.com", roleId, true, Set.of(departmentId));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updated);
        when(userMapper.toResponse(updated)).thenReturn(updatedResponse);

        UserResponse result = userService.update(userId, request);

        assertEquals("Jane Doe", result.name());
    }

    @Test
    void update_shouldThrowResourceNotFoundException_whenUserNotExists() {
        UpdateUserRequest request = new UpdateUserRequest("Jane Doe", null, null, null);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.update(userId, request));
    }

    @Test
    void delete_shouldDeactivateUser_whenExists() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.delete(userId);

        verify(userRepository).save(argThat(u -> !u.active()));
    }

    @Test
    void delete_shouldThrowResourceNotFoundException_whenNotExists() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.delete(userId));
    }
}