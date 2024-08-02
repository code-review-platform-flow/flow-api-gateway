package org.flow.gateway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.flow.gateway.dto.login.response.TokenResponseDto;
import org.flow.gateway.dto.users.UsersDto;
import org.flow.gateway.dto.usersessions.UserSessionsDto;
import org.flow.gateway.entity.UserSessionsEntity;
import org.flow.gateway.entity.UsersEntity;
import org.flow.gateway.mapper.UsersMapper;
import org.flow.gateway.security.JwtUtil;
import org.flow.gateway.service.usersessions.persistence.UserSessionsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UserSessionsService userSessionsService;
    private final ObjectMapper objectMapper;

    @PatchMapping("/refresh-token")
    public void reCreateAccessToken(HttpServletRequest request, HttpServletResponse response)
        throws IOException {

        String authHeader = request.getHeader("RefreshToken");

        jwtUtil.validateHeader(authHeader, response);
        String refreshToken = jwtUtil.getToken(authHeader);
        jwtUtil.isExpired(refreshToken, response);

        Long userId = jwtUtil.getUserId(refreshToken);
        System.out.println(userId);
        String email = jwtUtil.getEmail(refreshToken);
        String role = jwtUtil.getRole(refreshToken);

        String accessToken = jwtUtil.createAccessToken(userId, email, role);

        UsersDto usersDto = UsersDto.builder()
            .userId(userId)
            .build();

        UserSessionsDto userSessionsDto = userSessionsService.findByUserId(usersDto);
        System.out.println("1");
        userSessionsDto.setAccessToken(accessToken);

        UserSessionsDto usd = userSessionsService.modify(userSessionsDto, usersDto);
        System.out.println("2");
        System.out.println(usd.getAccessToken());
        System.out.println(usd.getRefreshToken());
        System.out.println(usd.getSessionId());
        System.out.println(usd.getVersion());

        TokenResponseDto tokenResponseDto = TokenResponseDto.builder()
            .accessToken(accessToken)
            .build();

        System.out.println(tokenResponseDto.getAccessToken());

        response.getWriter().write(objectMapper.writeValueAsString(tokenResponseDto));
    }

}
