package com.abrxu.upflow_feedback.application.service;

import com.abrxu.upflow_feedback.application.dto.request.CreateRoleRequest;
import com.abrxu.upflow_feedback.application.dto.request.UpdateRoleRequest;
import com.abrxu.upflow_feedback.application.dto.response.RoleResponse;
import com.abrxu.upflow_feedback.application.exception.DuplicateResourceException;
import com.abrxu.upflow_feedback.application.exception.ResourceNotFoundException;
import com.abrxu.upflow_feedback.application.mapper.RoleMapper;
import com.abrxu.upflow_feedback.application.port.out.RoleRepository;
import com.abrxu.upflow_feedback.application.port.out.UserRepository;
import com.abrxu.upflow_feedback.domain.Role;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleMapper roleMapper;

    @InjectMocks
    private RoleServiceImpl roleService;

    private UUID roleId;
    private Role role;
    private RoleResponse roleResponse;

    @BeforeEach
    void setUp() {
        roleId = UUID.randomUUID();
        role = new Role(roleId, "ADMIN");
        roleResponse = new RoleResponse(roleId, "ADMIN");
    }

    @Test
    void create_shouldCreateRole_whenNameIsUnique() {
        CreateRoleRequest request = new CreateRoleRequest("ADMIN");
        when(roleRepository.existsByName("ADMIN")).thenReturn(false);
        when(roleMapper.toDomain(request)).thenReturn(role);
        when(roleRepository.save(any(Role.class))).thenReturn(role);
        when(roleMapper.toResponse(role)).thenReturn(roleResponse);

        RoleResponse result = roleService.create(request);

        assertNotNull(result);
        assertEquals("ADMIN", result.name());
        verify(roleRepository).save(any(Role.class));
    }

    @Test
    void create_shouldThrowDuplicateResourceException_whenNameExists() {
        CreateRoleRequest request = new CreateRoleRequest("ADMIN");
        when(roleRepository.existsByName("ADMIN")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> roleService.create(request));
        verify(roleRepository, never()).save(any());
    }

    @Test
    void getById_shouldReturnRole_whenExists() {
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(roleMapper.toResponse(role)).thenReturn(roleResponse);

        RoleResponse result = roleService.getById(roleId);

        assertNotNull(result);
        assertEquals(roleId, result.id());
    }

    @Test
    void getById_shouldThrowResourceNotFoundException_whenNotExists() {
        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> roleService.getById(roleId));
    }

    @Test
    void getAll_shouldReturnPagedRoles() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Role> rolePage = new PageImpl<>(List.of(role));
        when(roleRepository.findAll(pageable)).thenReturn(rolePage);
        when(roleMapper.toResponse(role)).thenReturn(roleResponse);

        Page<RoleResponse> result = roleService.getAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void update_shouldUpdateRole_whenExistsAndNameUnique() {
        UpdateRoleRequest request = new UpdateRoleRequest("MANAGER");
        Role updatedRole = new Role(roleId, "MANAGER");
        RoleResponse updatedResponse = new RoleResponse(roleId, "MANAGER");

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(roleRepository.existsByName("MANAGER")).thenReturn(false);
        when(roleRepository.save(any(Role.class))).thenReturn(updatedRole);
        when(roleMapper.toResponse(updatedRole)).thenReturn(updatedResponse);

        RoleResponse result = roleService.update(roleId, request);

        assertEquals("MANAGER", result.name());
    }

    @Test
    void update_shouldThrowResourceNotFoundException_whenNotExists() {
        UpdateRoleRequest request = new UpdateRoleRequest("MANAGER");
        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> roleService.update(roleId, request));
    }

    @Test
    void update_shouldThrowDuplicateResourceException_whenNewNameExists() {
        UpdateRoleRequest request = new UpdateRoleRequest("MANAGER");
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(roleRepository.existsByName("MANAGER")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> roleService.update(roleId, request));
    }

    @Test
    void delete_shouldDeleteRole_whenExistsAndNoUsersAssigned() {
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(userRepository.existsByRoleId(roleId)).thenReturn(false);

        roleService.delete(roleId);

        verify(roleRepository).deleteById(roleId);
    }

    @Test
    void delete_shouldThrowResourceNotFoundException_whenNotExists() {
        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> roleService.delete(roleId));
    }

    @Test
    void delete_shouldThrowIllegalArgumentException_whenUsersAssigned() {
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(userRepository.existsByRoleId(roleId)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> roleService.delete(roleId));
        verify(roleRepository, never()).deleteById(any());
    }
}