package com.abrxu.upflow.user.repositories;

import com.abrxu.upflow.user.domain.User;
import com.abrxu.upflow.user.dtos.UserResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("""
            SELECT new com.abrxu.upflow.user.dtos.UserResponseDTO(u.id, c.username, u.name, u.lastName,
                                                                  c.email, u.createdAt, u.department)
            FROM User u
            JOIN u.credentials c
            JOIN u.department d
            WHERE (:search IS NULL OR
                   :search = '' OR
                   UPPER(u.name) LIKE UPPER(CONCAT('%', :search, '%')) OR
                   UPPER(u.lastName) LIKE UPPER(CONCAT('%', :search, '%')))
            """)
    Page<UserResponseDTO> getPaginatedUsers(@Param("search") String search, Pageable pageable);

    @Query("""
            FROM User u
            WHERE u.id IN :usersIds
            """)
    List<User> getUsersByIds(List<Long> usersIds);

}
