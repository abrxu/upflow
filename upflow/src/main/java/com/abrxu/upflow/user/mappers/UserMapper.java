package com.abrxu.upflow.user.mappers;

import com.abrxu.upflow.user.domain.User;
import com.abrxu.upflow.user.dtos.UserCreationDTO;
import com.abrxu.upflow.user.dtos.UserEditDTO;
import com.abrxu.upflow.user.dtos.UserResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDTO userToDTO(User user);

    User dtoToUser(UserCreationDTO dto);

    User editFromDTO(UserEditDTO dto, @MappingTarget User user);

}
