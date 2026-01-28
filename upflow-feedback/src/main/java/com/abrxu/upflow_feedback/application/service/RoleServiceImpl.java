package com.abrxu.upflow_feedback.application.service;

import com.abrxu.upflow_feedback.application.dto.request.CreateRoleRequest;
import com.abrxu.upflow_feedback.application.dto.request.UpdateRoleRequest;
import com.abrxu.upflow_feedback.application.dto.response.RoleResponse;
import com.abrxu.upflow_feedback.application.exception.DuplicateResourceException;
import com.abrxu.upflow_feedback.application.exception.ResourceNotFoundException;
import com.abrxu.upflow_feedback.application.mapper.RoleMapper;
import com.abrxu.upflow_feedback.application.port.in.RoleService;
import com.abrxu.upflow_feedback.application.port.out.RoleRepository;
import com.abrxu.upflow_feedback.application.port.out.UserRepository;
import com.abrxu.upflow_feedback.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final RoleMapper roleMapper;

    @Override
    public RoleResponse create(CreateRoleRequest request) {
        if (roleRepository.existsByName(request.name())) {
            throw new DuplicateResourceException("Role with name '" + request.name() + "' already exists");
        }
        Role role = roleMapper.toDomain(request);
        return roleMapper.toResponse(roleRepository.save(role));
    }

    @Override
    @Transactional(readOnly = true)
    public RoleResponse getById(UUID id) {
        return roleRepository.findById(id)
                .map(roleMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RoleResponse> getAll(Pageable pageable) {
        return roleRepository.findAll(pageable).map(roleMapper::toResponse);
    }

    @Override
    public RoleResponse update(UUID id, UpdateRoleRequest request) {
        Role existing = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));

        if (!existing.name().equals(request.name()) && roleRepository.existsByName(request.name())) {
            throw new DuplicateResourceException("Role with name '" + request.name() + "' already exists");
        }

        Role updated = new Role(existing.id(), request.name());
        return roleMapper.toResponse(roleRepository.save(updated));
    }

    @Override
    public void delete(UUID id) {
        if (roleRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Role not found with id: " + id);
        }
        if (userRepository.existsByRoleId(id)) {
            throw new IllegalArgumentException("Cannot delete role: users are still assigned to it");
        }
        roleRepository.deleteById(id);
    }

}