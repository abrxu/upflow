package com.abrxu.upflow.user.services;

import com.abrxu.upflow.department.services.DepartmentService;
import com.abrxu.upflow.exceptions.ErrorCode;
import com.abrxu.upflow.exceptions.ErrorCodeException;
import com.abrxu.upflow.user.domain.User;
import com.abrxu.upflow.user.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
                .orElseThrow(() -> new ErrorCodeException(ErrorCode.USER_NOT_FOUND));
    }

}
