package org.flow.gateway.security;


import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.flow.gateway.common.property.JwtTokenProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final JwtTokenProperty jwtTokenProperty;

    public JwtUtil(JwtTokenProperty jwtTokenProperty) {
        this.jwtTokenProperty = jwtTokenProperty;
        secretKey = new SecretKeySpec(jwtTokenProperty.getSecret().getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String getEmail(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
            .get("email", String.class);
    }

    public String getRole(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
            .get("role", String.class);
    }

    public Boolean isExpired(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
            .getExpiration().before(new Date());
    }

    public String createAccessToken(String email, String role) {
        return Jwts.builder()
            .claim("email", email)
            .claim("role", role)
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + jwtTokenProperty.getAccess().getExpiration()))
            .signWith(secretKey)
            .compact();
    }

    public String createRefreshToken(String email, String role){
        return Jwts.builder()
            .claim("email", email)
            .claim("role", role)
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + jwtTokenProperty.getRefresh().getExpiration()))
            .signWith(secretKey)
            .compact();
    }
}
