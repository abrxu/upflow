package com.abrxu.upflow_feedback.application.service;

import com.abrxu.upflow_feedback.application.dto.request.CreateUserRequest;
import com.abrxu.upflow_feedback.application.dto.request.UpdateUserRequest;
import com.abrxu.upflow_feedback.application.dto.response.UserResponse;
import com.abrxu.upflow_feedback.application.exception.DuplicateResourceException;
import com.abrxu.upflow_feedback.application.exception.ResourceNotFoundException;
import com.abrxu.upflow_feedback.application.mapper.UserMapper;
import com.abrxu.upflow_feedback.application.port.in.UserService;
import com.abrxu.upflow_feedback.application.port.out.DepartmentRepository;
import com.abrxu.upflow_feedback.application.port.out.RoleRepository;
import com.abrxu.upflow_feedback.application.port.out.UserRepository;
import com.abrxu.upflow_feedback.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponse create(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("User with email '" + request.email() + "' already exists");
        }
        if (roleRepository.findById(request.roleId()).isEmpty()) {
            throw new ResourceNotFoundException("Role not found with id: " + request.roleId());
        }
        validateDepartments(request.departmentIds());

        User user = userMapper.toDomain(request);
        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getById(UUID id) {
        return userRepository.findById(id)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> getAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getByDepartment(UUID departmentId) {
        if (!departmentRepository.existsById(departmentId)) {
            throw new ResourceNotFoundException("Department not found with id: " + departmentId);
        }
        return userMapper.toResponseList(userRepository.findByDepartmentId(departmentId));
    }

    @Override
    public UserResponse update(UUID id, UpdateUserRequest request) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        UUID roleId = request.roleId() != null ? request.roleId() : existing.roleId();
        if (request.roleId() != null && roleRepository.findById(request.roleId()).isEmpty()) {
            throw new ResourceNotFoundException("Role not found with id: " + request.roleId());
        }

        Set<UUID> departmentIds = request.departmentIds() != null ? request.departmentIds() : existing.departmentIds();
        if (request.departmentIds() != null) {
            validateDepartments(request.departmentIds());
        }

        User updated = new User(
                existing.id(),
                request.name() != null ? request.name() : existing.name(),
                existing.email(),
                roleId,
                request.active() != null ? request.active() : existing.active(),
                departmentIds
        );
        return userMapper.toResponse(userRepository.save(updated));
    }

    @Override
    public void delete(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        User deactivated = new User(
                user.id(),
                user.name(),
                user.email(),
                user.roleId(),
                false,
                user.departmentIds()
        );
        userRepository.save(deactivated);
    }

    private void validateDepartments(Set<UUID> departmentIds) {
        if (departmentIds == null || departmentIds.isEmpty()) {
            return;
        }
        for (UUID deptId : departmentIds) {
            if (!departmentRepository.existsById(deptId)) {
                throw new ResourceNotFoundException("Department not found with id: " + deptId);
            }
        }
    }

}