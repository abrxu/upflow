package com.abrxu.upflow.user.services;

import com.abrxu.upflow.department.services.DepartmentService;
import com.abrxu.upflow.user.domain.User;
import com.abrxu.upflow.user.domain.UserCredentials;
import com.abrxu.upflow.user.dtos.UserCreationDTO;
import com.abrxu.upflow.user.dtos.UserResponseDTO;
import com.abrxu.upflow.user.dtos.UserUpdateDTO;
import com.abrxu.upflow.user.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserManagementService {

    final UserRepository userRepository;
    final DepartmentService departmentService;

    public UserManagementService(UserRepository userRepository, DepartmentService departmentService) {
        this.userRepository = userRepository;
        this.departmentService = departmentService;
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

    @Transactional
    public UserResponseDTO updateUser(UserUpdateDTO dto) {
        var user = new User();
        BeanUtils.copyProperties(dto, user);
        userRepository.save(user);

        return UserResponseDTO.from(user);
    }

}
