package com.abrxu.upflow.user.services;

import com.abrxu.upflow.exceptions.ErrorCode;
import com.abrxu.upflow.exceptions.ErrorCodeException;
import com.abrxu.upflow.user.dtos.UserCreationDTO;
import com.abrxu.upflow.user.dtos.UserEditDTO;
import com.abrxu.upflow.user.dtos.UserResponseDTO;
import com.abrxu.upflow.user.mappers.UserCredentialsMapper;
import com.abrxu.upflow.user.mappers.UserMapper;
import com.abrxu.upflow.user.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    final UserRepository userRepository;
    final UserMapper userMapper;
    final UserCredentialsMapper userCredentialsMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper, UserCredentialsMapper userCredentialsMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userCredentialsMapper = userCredentialsMapper;
    }

    @Transactional
    public UserResponseDTO createUser(UserCreationDTO dto) {

        var user = userMapper.dtoToUser(dto);
        var credentials = userCredentialsMapper.userCreationDtoToCredentials(dto);

        user.setCredentials(credentials);
        credentials.setUser(user);

        userRepository.save(user);

        return UserResponseDTO.from(user);
    }

    public Page<UserResponseDTO> getPaginatedUsers(String search, Pageable pageable) {
        return userRepository.getPaginatedUsers(search, pageable);
    }

    @Transactional
    public UserResponseDTO editUser(UserEditDTO dto, Long userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ErrorCodeException(ErrorCode.USER_NOT_FOUND));

        userMapper.editFromDTO(dto, user);

        userRepository.save(user);

        return UserResponseDTO.from(user);
    }

    public UserResponseDTO getUser(Long userId) {
        return UserResponseDTO.from(userRepository.findById(userId)
                .orElseThrow(() -> new ErrorCodeException(ErrorCode.USER_NOT_FOUND)));
    }

    public void deleteUser(Long userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ErrorCodeException(ErrorCode.USER_NOT_FOUND));

        userRepository.delete(user);
    }

}
