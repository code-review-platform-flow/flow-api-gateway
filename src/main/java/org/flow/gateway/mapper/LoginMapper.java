package org.flow.gateway.mapper;

import org.flow.gateway.dto.jpa.JpaLoginUserDto;
import org.flow.gateway.entity.UserEntity;
import org.flow.gateway.entity.UserInfoEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LoginMapper {
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.password", target = "password")
    @Mapping(source = "userInfo.role", target = "role")
    JpaLoginUserDto toJpaLoginUserDto(UserEntity user, UserInfoEntity userInfo);

}
