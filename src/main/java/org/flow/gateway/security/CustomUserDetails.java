package org.flow.gateway.security;


import java.util.ArrayList;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.flow.gateway.dto.userinfo.UserInfoDto;
import org.flow.gateway.dto.users.UsersDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final UsersDto usersDto;
    private final UserInfoDto userInfoDto;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return userInfoDto.getRole();
            }
        });
        return collection;
    }

    @Override
    public String getPassword() {
        return usersDto.getPassword();
    }

    @Override
    public String getUsername() {
        return usersDto.getEmail();
    }

    public Long getUserId() { return usersDto.getUserId(); }
}
