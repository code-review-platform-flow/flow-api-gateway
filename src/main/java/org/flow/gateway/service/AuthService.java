package org.flow.gateway.service;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.flow.gateway.dto.login.response.TokenResponseDto;
import org.flow.gateway.dto.users.UsersDto;
import org.flow.gateway.dto.usersessions.UserSessionsDto;
import org.flow.gateway.security.JwtUtil;
import org.flow.gateway.service.usersessions.persistence.UserSessionsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
    private final UserSessionsService userSessionsService;

    public ResponseEntity<TokenResponseDto> reCreateAccessToken(String authHeader) throws IOException{

        String refreshToken = jwtUtil.getToken(authHeader);
        boolean checkRefresh = jwtUtil.isExpired(refreshToken);
        if (checkRefresh){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long userId = jwtUtil.getUserId(refreshToken);
        String email = jwtUtil.getEmail(refreshToken);
        String role = jwtUtil.getRole(refreshToken);

        String accessToken = jwtUtil.createAccessToken(userId, email, role);

        UsersDto usersDto = UsersDto.builder()
            .userId(userId)
            .build();

        UserSessionsDto userSessionsDto = userSessionsService.findByUserId(usersDto);
        userSessionsDto.setAccessToken(accessToken);

        userSessionsService.save(userSessionsDto, usersDto);

        TokenResponseDto tokenResponseDto = TokenResponseDto.builder()
            .accessToken(accessToken)
            .build();

        return ResponseEntity.ok(tokenResponseDto);
    }

}
