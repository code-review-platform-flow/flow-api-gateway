package org.flow.gateway.service.userinfo.persistence;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import org.flow.gateway.dto.userinfo.UserInfoDto;
import org.flow.gateway.mapper.UserInfoMapper;
import org.flow.gateway.repository.UserInfoRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserInfoService {

    private final UserInfoRepository userInfoRepository;
    private final UserInfoMapper userInfoMapper;

    public Mono<UserInfoDto> findByUserId(Long userId) {
        return userInfoRepository.findAllByUserId(userId)
            .collectList()
            .flatMap(userInfos -> {
                if (userInfos.isEmpty()) {
                    return Mono.error(new RuntimeException("UserInfo not found with userId: " + userId));
                }
                return Mono.just(userInfoMapper.toDto(userInfos.get(0)));
            });
    }
}
