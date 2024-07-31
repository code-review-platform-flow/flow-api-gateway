package org.flow.gateway.mapper;

import org.flow.gateway.dto.login.response.LoginResponseDto;
import org.flow.gateway.dto.users.UsersDto;
import org.flow.gateway.dto.usersessions.UserSessionsDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LoginMapper {

    @Mapping(source = "users.email", target = "email")
    @Mapping(source = "userSessions.accessToken", target = "AccessToken")
    @Mapping(source = "userSessions.refreshToken", target = "RefreshToken")
    LoginResponseDto toLoginResponseDto(UserSessionsDto userSessions, UsersDto users);

}
