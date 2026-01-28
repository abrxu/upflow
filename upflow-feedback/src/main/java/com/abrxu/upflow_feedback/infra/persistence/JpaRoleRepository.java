package com.abrxu.upflow_feedback.infra.persistence;

import com.abrxu.upflow_feedback.application.port.out.RoleRepository;
import com.abrxu.upflow_feedback.domain.Role;
import com.abrxu.upflow_feedback.infra.RoleEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaRoleRepository implements RoleRepository {

    private final SpringDataRoleRepository jpaRepository;

    @Override
    public Role save(Role role) {
        RoleEntity entity = RoleEntity.fromDomain(role);
        return jpaRepository.save(entity).toDomain();
    }

    @Override
    public Optional<Role> findById(UUID id) {
        return jpaRepository.findById(id).map(RoleEntity::toDomain);
    }

    @Override
    public Page<Role> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(RoleEntity::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }

}