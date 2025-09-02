package com.abrxu.upflow.user.mappers;

import com.abrxu.upflow.user.domain.User;
import com.abrxu.upflow.user.domain.UserCredentials;
import com.abrxu.upflow.user.domain.UserDepartment;
import com.abrxu.upflow.user.dtos.UserCreationDTO;
import com.abrxu.upflow.user.dtos.UserEditDTO;
import com.abrxu.upflow.user.dtos.UserResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", uses = {UserDepartmentMapper.class})
public interface UserMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "credentials.username", target = "username")
    @Mapping(source = "credentials.email", target = "email")
    UserResponseDTO userToDTO(User user);

    @Mapping(ignore = true, target = "associations")
    User dtoToUser(UserCreationDTO dto);

    @Mapping(ignore = true, target = "associations")
    User editFromDTO(UserEditDTO dto, @MappingTarget User user);

}
