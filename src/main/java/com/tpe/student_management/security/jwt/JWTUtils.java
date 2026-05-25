package com.tpe.student_management.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Component
public class JWTUtils {

    @Value("${backendapi.app.jwtSecret}")
    private String secret;

    @Value("${backendapi.app.accessTokenExpiration}")
    private long accessExpiration;

    @Value("${backendapi.app.refreshTokenExpiration}")
    private long refreshExpiration;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    //!!! 1 - Generate access token
    public String generateAccessToken(String username, String role){
        return generateJWT(username, Map.of("role", role), accessExpiration);
    }

    //!!! 2 - Generate refresh token
    public String generateRefreshToken(String username, String role){
        return generateJWT(username, Map.of("role", role), refreshExpiration);
    }

    //!!! 3 - Private token builder
    private String generateJWT(String subject, Map<String, Object> claims, long expirationMs) {
        return Jwts.builder()
                .subject(subject)
                .claims(claims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(secretKey)
                .compact();
    }

    //!!! 4 - Extract claims
    public Claims extractClaims(String token){
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    //!!! 5 - Validate token
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
