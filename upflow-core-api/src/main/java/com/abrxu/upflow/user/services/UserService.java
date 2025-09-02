package com.abrxu.upflow.user.services;

import com.abrxu.upflow.department.domain.Department;
import com.abrxu.upflow.department.repositories.DepartmentRepository;
import com.abrxu.upflow.exceptions.ErrorCode;
import com.abrxu.upflow.exceptions.ErrorCodeException;
import com.abrxu.upflow.user.domain.*;
import com.abrxu.upflow.user.dtos.*;
import com.abrxu.upflow.user.mappers.UserCredentialsMapper;
import com.abrxu.upflow.user.mappers.UserMapper;
import com.abrxu.upflow.user.repositories.UserRepository;
import org.mapstruct.ap.shaded.freemarker.log.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final Logger logger = Logger.getLogger(UserService.class.getName());

    final UserRepository userRepository;
    final UserMapper userMapper;
    final UserCredentialsMapper userCredentialsMapper;
    final DepartmentRepository departmentRepository;

    public UserService(UserRepository userRepository, UserMapper userMapper, UserCredentialsMapper userCredentialsMapper, DepartmentRepository departmentRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userCredentialsMapper = userCredentialsMapper;
        this.departmentRepository = departmentRepository;
    }

    @Transactional
    public UserResponseDTO createUser(UserCreationDTO dto) {

        logger.debug("Inicializando UserResponseDTO.createUser() para - " + dto.username());

        User user = userMapper.dtoToUser(dto);
        UserCredentials credentials = userCredentialsMapper.userCreationDtoToCredentials(dto);
        user.setCredentials(credentials);
        credentials.setUser(user);

        userRepository.save(user);

        Set<UserDepartment> departments = new HashSet<>();

        for (UserDepartmentUserCreationDTO dep : dto.associations()) {
            Department department = departmentRepository.findById(dep.departmentId())
                    .orElseThrow(() -> new ErrorCodeException(ErrorCode.DEPARTMENT_NOT_FOUND));

            UserDepartment userDepartment = new UserDepartment(user, department, dep.role() != null ? dep.role() : DepartmentRole.MEMBER);

            departments.add(userDepartment);
        }

        user.setAssociations(departments);

        userRepository.save(user);

        return userMapper.userToDTO(user);
    }

    @Transactional
    public UserResponseDTO editUser(UserEditDTO dto, Long userId) {
        var user = userRepository.findByIdWithAssociations(userId)
                .orElseThrow(() -> new ErrorCodeException(ErrorCode.USER_NOT_FOUND));

        userMapper.editFromDTO(dto, user);

        Map<Long, UserDepartment> existingAssociations = user.getAssociations().stream()
                .collect(Collectors.toMap(ud -> ud.getDepartment().getId(), ud -> ud));

        Set<UserDepartment> finalAssociations = new HashSet<>();

        for (UserDepartmentCreationDTO dep : dto.associations()) {
            Department department = departmentRepository.findById(dep.departmentId())
                    .orElseThrow(() -> new ErrorCodeException(ErrorCode.DEPARTMENT_NOT_FOUND));

            UserDepartment existingAssociation = existingAssociations.remove(dep.departmentId());

            if (existingAssociation != null) {
                existingAssociation.setRole(dep.role() != null ? dep.role() : DepartmentRole.MEMBER);
                finalAssociations.add(existingAssociation);
            } else {
                UserDepartment newAssociation = new UserDepartment(user, department, dep.role() != null ? dep.role() : DepartmentRole.MEMBER);
                finalAssociations.add(newAssociation);
            }
        }

        user.getAssociations().clear();
        user.getAssociations().addAll(finalAssociations);

        userRepository.save(user);

        return userMapper.userToDTO(user);
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDTO> getPaginatedUsers(String search, Pageable pageable) {

        Page<User> userPage = userRepository.findUsersWithPagination(search, pageable);
        List<User> usersOnPage = userPage.getContent();

        if (usersOnPage.isEmpty()) {
            return Page.empty(pageable);
        }

        List<Long> userIds = usersOnPage.stream()
                .map(User::getId)
                .toList();

        List<UserDepartment> associations = userRepository.findDepartmentAssociationsForUsers(userIds);

        Map<Long, List<UserDepartment>> associationsByUserId = associations.stream()
                .collect(Collectors.groupingBy(ud -> ud.getUser().getId()));

        List<UserResponseDTO> dtos = usersOnPage.stream()
                .map(user -> {
                    List<UserDepartment> userAssociations = associationsByUserId.getOrDefault(user.getId(), Collections.emptyList());
                    return userMapper.userToDTO(user);
                }).toList();

        return new PageImpl<>(dtos, pageable, userPage.getTotalElements());
    }

    public UserResponseDTO getUser(Long userId) {
        var user = userRepository.findByIdWithAssociations(userId)
                .orElseThrow(() -> new ErrorCodeException(ErrorCode.USER_NOT_FOUND));

        return userMapper.userToDTO(user);
    }

    public void deleteUser(Long userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ErrorCodeException(ErrorCode.USER_NOT_FOUND));

        userRepository.delete(user);
    }

}
