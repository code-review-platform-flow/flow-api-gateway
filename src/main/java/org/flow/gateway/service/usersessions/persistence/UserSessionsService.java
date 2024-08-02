package org.flow.gateway.service.usersessions.persistence;

import jakarta.persistence.EntityNotFoundException;
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
public class UserSessionsService {

    private final UserSessionsRepository userSessionsRepository;
    private final UserSessionsMapper userSessionsMapper;
    private final UsersMapper usersMapper;

    @Transactional
    public UserSessionsDto modify(UserSessionsDto userSessionsDto, UsersDto usersDto){
        UsersEntity usersEntity = usersMapper.toEntity(usersDto);
        UserSessionsEntity userSessionsEntity = userSessionsMapper.toEntity(userSessionsDto);
        userSessionsEntity.setUser(usersEntity);
        userSessionsEntity.setUseYn(true);
        userSessionsRepository.save(userSessionsEntity);
        System.out.println("success save");
        return userSessionsMapper.toDto(userSessionsEntity);
    }


    public UserSessionsDto findByUserId(UsersDto usersDto){
        UsersEntity usersEntity = usersMapper.toEntity(usersDto);
        UserSessionsEntity userSessionsEntity = userSessionsRepository.findByUserId(usersEntity.getUserId())
            .orElseThrow(() -> new EntityNotFoundException("UserSession not found with user"));
        return userSessionsMapper.toDto(userSessionsEntity);
    }

}
