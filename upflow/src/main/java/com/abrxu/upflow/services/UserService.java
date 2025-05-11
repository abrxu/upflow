package com.abrxu.upflow.services;

import com.abrxu.upflow.models.user.User;
import com.abrxu.upflow.models.user.dtos.UserCreationDTO;
import com.abrxu.upflow.models.user.dtos.UserResponseDTO;
import com.abrxu.upflow.models.user.dtos.UserUpdateDTO;
import com.abrxu.upflow.models.user.UserCredentials;
import com.abrxu.upflow.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    final UserRepository userRepository;
    final DepartmentService departmentService;
    final UserCredentialsService userCredentialsService;

    public UserService(UserRepository userRepository, DepartmentService departmentService, UserCredentialsService userCredentialsService) {
        this.userRepository = userRepository;
        this.departmentService = departmentService;
        this.userCredentialsService = userCredentialsService;
    }

    @Transactional
    public UserResponseDTO createUser(UserCreationDTO dto) {
        var user = new User();
        var userCredentials = new UserCredentials();
        BeanUtils.copyProperties(dto, user);
        BeanUtils.copyProperties(dto, userCredentials);
        user.setCredentials(userCredentials);

        Optional.ofNullable(dto.departmentId())
            .map(departmentService::findDepartmentById)
            .ifPresent(user::setDepartment);

        userRepository.save(user);

        return UserResponseDTO.from(user);
    }

    public UserResponseDTO updateUser(UserUpdateDTO dto) {
        var user = new User();
        BeanUtils.copyProperties(dto, user);
        userRepository.save(user);

        return UserResponseDTO.from(user);
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
