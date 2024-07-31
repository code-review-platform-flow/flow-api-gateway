package org.flow.gateway.service.userinfo.persistence;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.flow.gateway.dto.userinfo.UserInfoDto;
import org.flow.gateway.entity.UsersEntity;
import org.flow.gateway.mapper.UserInfoMapper;
import org.flow.gateway.repository.UserInfoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserInfoService {

    private final UserInfoRepository userInfoRepository;
    private final UserInfoMapper userInfoMapper;

    @Transactional
    public UserInfoDto findByUserId(Long userId){
        return userInfoMapper.toDto(userInfoRepository.findByUserId(userId)
            .orElseThrow(() -> new EntityNotFoundException("UserInfo not found with user")));
    }
}
