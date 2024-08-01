package org.flow.gateway.mapper;

import org.flow.gateway.common.mapper.GenericMapper;
import org.flow.gateway.dto.userinfo.UserInfoDto;
import org.flow.gateway.entity.UserInfoEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserInfoMapper extends GenericMapper<UserInfoDto, UserInfoEntity> {

}
