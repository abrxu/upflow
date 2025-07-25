package com.abrxu.upflow.user.mappers;

import com.abrxu.upflow.user.domain.UserCredentials;
import com.abrxu.upflow.user.dtos.UserCreationDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserCredentialsMapper {

    UserCredentials userCreationDtoToCredentials(UserCreationDTO dto);

}
