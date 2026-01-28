package com.abrxu.upflow_feedback.application.port.out;

import com.abrxu.upflow_feedback.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(UUID id);

    Page<User> findAll(Pageable pageable);

    List<User> findByDepartmentId(UUID departmentId);

    void deleteById(UUID id);

    boolean existsByEmail(String email);

    boolean existsByRoleId(UUID roleId);

}