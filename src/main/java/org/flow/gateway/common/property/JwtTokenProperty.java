package org.flow.gateway.common.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.jwt")
public class JwtTokenProperty {
    private String secret;
    private AccessToken access;
    private RefreshToken refresh;

    @Getter
    @Setter
    public static class AccessToken{
        private Long expiration;
    }

    @Getter
    @Setter
    public static class RefreshToken{
        private Long expiration;
    }
}
