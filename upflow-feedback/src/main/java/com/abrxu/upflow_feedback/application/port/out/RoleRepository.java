package com.abrxu.upflow_feedback.application.port.out;

import com.abrxu.upflow_feedback.domain.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository {

    Role save(Role role);

    Optional<Role> findById(UUID id);

    Page<Role> findAll(Pageable pageable);

    void deleteById(UUID id);

    boolean existsByName(String name);

}