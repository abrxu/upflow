package com.abrxu.upflow.user.repositories;

import com.abrxu.upflow.user.domain.User;
import com.abrxu.upflow.user.domain.UserDepartment;
import com.abrxu.upflow.user.dtos.UserResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("""
           SELECT u
           FROM User u
           LEFT JOIN FETCH u.credentials
           LEFT JOIN FETCH u.associations associations
           LEFT JOIN FETCH associations.department
           WHERE u.id = :id
           """)
    Optional<User> findByIdWithAssociations(@Param("id") Long id);

    @Query(value = """
            SELECT u
            FROM User u
            JOIN FETCH u.credentials c
            WHERE (:search IS NULL OR
                   :search = '' OR
                   UPPER(u.name) LIKE UPPER(CONCAT('%', :search, '%')) OR
                   UPPER(u.lastName) LIKE UPPER(CONCAT('%', :search, '%')))
            """,
            countQuery = """
            SELECT COUNT(u)
            FROM User u
            WHERE (:search IS NULL OR
                   :search = '' OR
                   UPPER(u.name) LIKE UPPER(CONCAT('%', :search, '%')) OR
                   UPPER(u.lastName) LIKE UPPER(CONCAT('%', :search, '%')))
            """)
    Page<User> findUsersWithPagination(@Param("search") String search, Pageable pageable);

    @Query("""
            SELECT ud
            FROM UserDepartment ud
            JOIN FETCH ud.department
            JOIN FETCH ud.user
            WHERE ud.user.id IN :userIds
            """)
    List<UserDepartment> findDepartmentAssociationsForUsers(@Param("userIds") List<Long> userIds);


}
