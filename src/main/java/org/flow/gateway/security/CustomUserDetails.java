package org.flow.gateway.security;


import java.util.ArrayList;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.flow.gateway.dto.jpa.JpaLoginUserDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final JpaLoginUserDto jpaLoginUserDto;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return jpaLoginUserDto.getRole();
            }
        });
        return collection;
    }

    @Override
    public String getPassword() {
        return jpaLoginUserDto.getPassword();
    }

    @Override
    public String getUsername() {
        return jpaLoginUserDto.getEmail();
    }
}
