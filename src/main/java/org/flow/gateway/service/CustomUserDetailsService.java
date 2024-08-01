package org.flow.gateway.service;

import lombok.RequiredArgsConstructor;
import org.flow.gateway.dto.userinfo.UserInfoDto;
import org.flow.gateway.dto.users.UsersDto;
import org.flow.gateway.security.CustomUserDetails;
import org.flow.gateway.service.userinfo.persistence.UserInfoService;
import org.flow.gateway.service.users.persistence.UsersService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersService usersService;
    private final UserInfoService userInfoService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UsersDto usersDto = usersService.findByEmail(email);
        System.out.println("findByEmail 성공");
        UserInfoDto userInfoDto = userInfoService.findByUserId(usersDto.getUserId());

        return new CustomUserDetails(usersDto, userInfoDto);
    }
}
