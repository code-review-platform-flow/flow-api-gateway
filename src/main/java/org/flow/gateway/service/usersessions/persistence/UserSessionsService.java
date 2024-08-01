package org.flow.gateway.service.usersessions.persistence;

import lombok.RequiredArgsConstructor;
import org.flow.gateway.dto.usersessions.UserSessionsDto;
import org.flow.gateway.entity.UserSessionsEntity;
import org.flow.gateway.mapper.UserSessionsMapper;
import org.flow.gateway.repository.UserSessionsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserSessionsService {

    private final UserSessionsRepository userSessionsRepository;
    private final UserSessionsMapper userSessionsMapper;

    @Transactional
    public UserSessionsDto save(UserSessionsDto userSessionsDto){
        UserSessionsEntity userSessionsEntity = userSessionsMapper.toEntity(userSessionsDto);
        userSessionsRepository.save(userSessionsEntity);
        return userSessionsMapper.toDto(userSessionsEntity);
    }
}
