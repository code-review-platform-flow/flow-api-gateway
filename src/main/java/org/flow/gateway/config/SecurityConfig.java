package org.flow.gateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.flow.gateway.security.JwtFilter;
import org.flow.gateway.security.JwtUtil;
import org.flow.gateway.security.LoginFilter;
import org.flow.gateway.service.usersessions.persistence.UserSessionsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    private final UserSessionsService userSessionsService;
    private final ObjectMapper objectMapper;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/auth/register/**", "/auth/refresh-token/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf((auth) -> auth.disable());
        http
            .formLogin((auth) -> auth.disable());
        http
            .httpBasic((auth) -> auth.disable());
        http
            .sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        LoginFilter loginFilter = new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil,
            userSessionsService, objectMapper);
        loginFilter.setFilterProcessesUrl("/auth/login");

        http
            .addFilterBefore(new JwtFilter(jwtUtil), LoginFilter.class);
        http
            .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);

        http
            .authorizeHttpRequests((auth) -> auth
                .requestMatchers("/auth/login/**", "/auth/register/**", "/auth/refresh-token/**").permitAll()
            );
        return http.build();
    }
}
