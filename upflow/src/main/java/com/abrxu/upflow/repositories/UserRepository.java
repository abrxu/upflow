package com.abrxu.upflow.repositories;

import com.abrxu.upflow.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
