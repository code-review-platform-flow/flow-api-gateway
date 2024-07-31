package org.flow.gateway.service.usersessions;


import lombok.RequiredArgsConstructor;
import org.flow.gateway.dto.users.UsersDto;
import org.flow.gateway.dto.usersessions.UserSessionsDto;
import org.flow.gateway.entity.UserSessionsEntity;
import org.flow.gateway.entity.UsersEntity;
import org.flow.gateway.mapper.UserSessionsMapper;
import org.flow.gateway.mapper.UsersMapper;
import org.flow.gateway.repository.UserSessionsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserSessionsLoginService {

    private final UserSessionsRepository userSessionsRepository;
    private final UserSessionsMapper userSessionsMapper;
    private final UsersMapper usersMapper;

    @Transactional
    public UserSessionsDto save(UserSessionsDto userSessionsDto, UsersDto usersDto){
        UsersEntity usersEntity = usersMapper.toEntity(usersDto);
        UserSessionsEntity userSessionsEntity = userSessionsMapper.toEntity(userSessionsDto);
        userSessionsEntity.setUser(usersEntity);
        userSessionsRepository.save(userSessionsEntity);
        return userSessionsMapper.toDto(userSessionsEntity);
    }

}
