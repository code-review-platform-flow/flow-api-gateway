package org.flow.gateway.security;


import io.micrometer.common.util.StringUtils;
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

    private static final String AUTH_LOGIN_PATH = "/auth/login";
    private static final String ACCESS_TOKEN_HEADER = "AccessToken";

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        String path = request.getRequestURI();

        if (path.equals(AUTH_LOGIN_PATH)) {
            // Skip filter for login
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader(ACCESS_TOKEN_HEADER);

        if (authHeader == null || authHeader.isEmpty()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing AccessToken header");
            return;
        }

        try {
            jwtUtil.validateHeader(authHeader);
            String accessToken = jwtUtil.getToken(authHeader);
            jwtUtil.isExpired(accessToken);

            Long userId = jwtUtil.getUserId(accessToken);
            String email = jwtUtil.getEmail(accessToken);
            String role = jwtUtil.getRole(accessToken);

            UsersDto usersDto = UsersDto.builder()
                .userId(userId)
                .email(email)
                .password("tempPassword")  // Ensure this placeholder password is handled securely
                .build();
            UserInfoDto userInfoDto = UserInfoDto.builder()
                .role(role)
                .build();

            CustomUserDetails customUserDetails = new CustomUserDetails(usersDto, userInfoDto);

            Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }
}