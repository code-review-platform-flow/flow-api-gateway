package org.flow.gateway.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.flow.gateway.dto.login.response.LoginResponseDto;
import org.flow.gateway.dto.users.UsersDto;
import org.flow.gateway.dto.usersessions.UserSessionsDto;
import org.flow.gateway.service.usersessions.persistence.UserSessionsLoginService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserSessionsLoginService userSessionsLoginService;
    private final ObjectMapper objectMapper;

    @Override
    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter("email");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response) throws AuthenticationException {

        String email = obtainUsername(request);
        String password = obtainPassword(request);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password, null);

        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain, Authentication authResult)
        throws IOException, ServletException {

        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();
        Long userId = customUserDetails.getUserId();
        String email = customUserDetails.getUsername();
        String role = authResult.getAuthorities().iterator().next().getAuthority();

        String accessToken = jwtUtil.createAccessToken(userId, email, role);
        String refreshToken = jwtUtil.createRefreshToken(userId, email, role);

        UsersDto usersDto = UsersDto.builder()
            .userId(userId)
            .build();

        UserSessionsDto userSessionsDto = userSessionsLoginService.findByUserId(usersDto);
        if (userSessionsDto == null){
            UserSessionsDto userSessions = UserSessionsDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

            userSessionsLoginService.save(userSessions, usersDto);
        }
        else{
            userSessionsDto.setAccessToken(accessToken);
            userSessionsDto.setRefreshToken(refreshToken);

            userSessionsLoginService.modify(userSessionsDto, usersDto);
        }

        LoginResponseDto loginResponseDto = LoginResponseDto.builder()
            .email(email)
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();

        response.getWriter().write(objectMapper.writeValueAsString(loginResponseDto));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, AuthenticationException failed)
        throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
