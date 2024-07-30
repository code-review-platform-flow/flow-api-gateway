package org.flow.gateway.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.flow.gateway.dto.jpa.JpaLoginUserDto;
import org.flow.gateway.entity.UserEntity;
import org.flow.gateway.entity.UserInfoEntity;
import org.flow.gateway.mapper.LoginMapper;
import org.flow.gateway.repository.UserInfoRepository;
import org.flow.gateway.repository.UserRepository;
import org.flow.gateway.security.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;
    private final LoginMapper loginMapper;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        UserInfoEntity userInfo = userInfoRepository.findByUser(user)
            .orElseThrow(() -> new EntityNotFoundException("UserInfo not found with user"));

        JpaLoginUserDto jpaLoginUserDto = loginMapper.toJpaLoginUserDto(user, userInfo);

        return new CustomUserDetails(jpaLoginUserDto);
    }
}
