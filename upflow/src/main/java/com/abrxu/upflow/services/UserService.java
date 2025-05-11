package com.abrxu.upflow.services;

import com.abrxu.upflow.models.user.User;
import com.abrxu.upflow.models.dtos.UserCreationDTO;
import com.abrxu.upflow.models.dtos.UserResponseDTO;
import com.abrxu.upflow.models.dtos.UserUpdateDTO;
import com.abrxu.upflow.repositories.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDTO createUser(UserCreationDTO dto) {
        var user = new User();
        BeanUtils.copyProperties(dto, user);
        userRepository.save(user);
        var response = new UserResponseDTO();
        BeanUtils.copyProperties(user, response);
        return response;
    }

    public UserResponseDTO updateUser(UserUpdateDTO dto) {
        var user = new User();
        BeanUtils.copyProperties(dto, user);
        userRepository.save(user);
        var response = new UserResponseDTO();
        BeanUtils.copyProperties(user, response);
        return response;
    }

}
