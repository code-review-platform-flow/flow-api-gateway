package org.flow.gateway.mapper;

import org.flow.gateway.common.mapper.GenericMapper;
import org.flow.gateway.dto.users.UsersDto;
import org.flow.gateway.entity.UsersEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsersMapper extends GenericMapper<UsersDto, UsersEntity> {

}
