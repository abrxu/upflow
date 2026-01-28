package com.abrxu.upflow_feedback.infra.persistence;

import com.abrxu.upflow_feedback.application.port.out.UserRepository;
import com.abrxu.upflow_feedback.domain.User;
import com.abrxu.upflow_feedback.infra.DepartmentEntity;
import com.abrxu.upflow_feedback.infra.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class JpaUserRepository implements UserRepository {

    private final SpringDataUserRepository jpaRepository;
    private final SpringDataDepartmentRepository departmentRepository;

    @Override
    public User save(User user) {
        Set<DepartmentEntity> departmentEntities = new HashSet<>();
        if (user.departmentIds() != null && !user.departmentIds().isEmpty()) {
            departmentEntities = new HashSet<>(departmentRepository.findAllByIdIn(user.departmentIds()));
        }
        UserEntity entity = UserEntity.fromDomain(user, departmentEntities);
        return jpaRepository.save(entity).toDomain();
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpaRepository.findById(id).map(UserEntity::toDomain);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(UserEntity::toDomain);
    }

    @Override
    public List<User> findByDepartmentId(UUID departmentId) {
        return jpaRepository.findByDepartmentId(departmentId).stream()
                .map(UserEntity::toDomain)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByRoleId(UUID roleId) {
        return jpaRepository.existsByRoleId(roleId);
    }

}