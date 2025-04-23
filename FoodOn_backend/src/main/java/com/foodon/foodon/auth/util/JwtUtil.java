package com.foodon.foodon.auth.util;

import static com.foodon.foodon.auth.exception.AuthException.AuthUnauthorizedException;
import com.foodon.foodon.auth.domain.UserTokens;
import com.foodon.foodon.auth.exception.AuthErrorCode;
import com.foodon.foodon.auth.exception.AuthException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final Long accessTokenExpiry;
    private final Long refreshTokenExpiry;

    public JwtUtil(
            @Value("${spring.auth.jwt.secret-key}") final String secretKey,
            @Value("${spring.auth.jwt.access-token-expiry}") final Long accessTokenExpiry,
            @Value("${spring.auth.jwt.refresh-token-expiry}") final Long refreshTokenExpiry
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpiry = accessTokenExpiry;
        this.refreshTokenExpiry = refreshTokenExpiry;
    }

    // ---------- * 토큰 생성 * ---------- //

    public UserTokens createUserToken(String subject) {
        String refreshToken = createToken("", refreshTokenExpiry);
        String accessToken = createToken(subject, accessTokenExpiry);
        return new UserTokens(refreshToken, accessToken);
    }

    private String createToken(String subject, Long expiredMs) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

    public String reissueAccessToken(String subject) {
        return createToken(subject, accessTokenExpiry);
    }

    // ---------- * 토큰 정보 추출 (Subject) * ---------- //

    public String getSubject(String token) {
        return parseToken(token)
                .getBody().getSubject();
    }

    private Jws<Claims> parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
    }

    // ---------- * 토큰 검증 (keys, expiry) * ---------- //

    public void validateRefreshToken(String refreshToken) {
        try {
            parseToken(refreshToken);
        } catch (JwtException e) {
            throw new AuthUnauthorizedException(AuthErrorCode.INVALID_REFRESH_TOKEN);
        }
    }

    public boolean isAccessTokenValid(String accessToken) {
        try {
            parseToken(accessToken);
        } catch (JwtException e) {
            return false;
        }
        return true;
    }

    public boolean isAccessTokenExpired(String accessToken) {
        try {
            parseToken(accessToken);
        } catch (ExpiredJwtException e) {
            return true;
        }
        return false;
    }
}