package org.flow.gateway.mapper;

import org.flow.gateway.common.mapper.GenericMapper;
import org.flow.gateway.dto.usersessions.UserSessionsDto;
import org.flow.gateway.entity.UserSessionsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.jpa.repository.Query;

@Mapper(componentModel = "spring")
public interface UserSessionsMapper extends GenericMapper<UserSessionsDto, UserSessionsEntity> {


}
