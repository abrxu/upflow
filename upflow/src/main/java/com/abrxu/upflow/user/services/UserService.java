package com.abrxu.upflow.user.services;

import com.abrxu.upflow.department.services.DepartmentService;
import com.abrxu.upflow.user.domain.User;
import com.abrxu.upflow.user.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    final UserRepository userRepository;
    final DepartmentService departmentService;

    public UserService(UserRepository userRepository, DepartmentService departmentService) {
        this.userRepository = userRepository;
        this.departmentService = departmentService;
    }

    public List<User> findUsersByIds(List<Long> ids) {
        var users = userRepository.findAllById(ids);

        if (users.size() != ids.size()) {
            var foundIds = users.stream().map(User::getId).toList();
            var missingIds = ids.stream().filter(id -> !foundIds.contains(id)).toList();
            throw new RuntimeException("Users not found: " + missingIds);
        }

        return users;
    }

    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found."));
    }

}
