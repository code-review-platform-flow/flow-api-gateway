package org.flow.gateway.common.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
		http
			.csrf(ServerHttpSecurity.CsrfSpec::disable)  // CSRF 비활성화
			.cors(corsSpec -> corsSpec.configurationSource(corsConfigurationSource()))  // CORS 설정 추가
			.authorizeExchange(exchanges -> exchanges
				.anyExchange().permitAll()
			)
			.formLogin(Customizer.withDefaults())
			.httpBasic(Customizer.withDefaults());

		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration corsConfig = new CorsConfiguration();
		corsConfig.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://your-other-allowed-origin.com"));
		corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
		corsConfig.setAllowedHeaders(Arrays.asList("*"));
		corsConfig.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfig);
		return source;
	}
}

