package org.flow.gateway.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.flow.gateway.dto.userinfo.UserInfoDto;
import org.flow.gateway.dto.users.UsersDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        if (path.equals("/auth/login")) {
            // 로그인은 Filter 건너뛰기
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("AccessToken");

        jwtUtil.validateHeader(authHeader, response); //header 검사
        String accessToken = jwtUtil.getToken(authHeader); //Bearer 떼기
        jwtUtil.isExpired(accessToken, response); //token의 만료시간 검사

        Long userId = jwtUtil.getUserId(accessToken);
        String email = jwtUtil.getEmail(accessToken);
        String role = jwtUtil.getRole(accessToken);

        UsersDto usersDto = UsersDto.builder()
            .userId(userId)
            .email(email)
            .password("tempPassword")
            .build();
        UserInfoDto userInfoDto = UserInfoDto.builder()
            .role(role)
            .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(usersDto, userInfoDto);

        Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
