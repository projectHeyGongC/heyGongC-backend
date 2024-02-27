package com.heygongc.user.application;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Getter
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET_KEY;
    @Value("${jwt.access.expiration}")
    private Long ACCESS_EXP;
    @Value("${jwt.refresh.expiration}")
    private Long REFRESH_EXP;

    public AuthToken generateAuthToken(Long userSeq, String deviceId) {
        // jwt 토큰 발급
        String accessToken = generateAccessToken(String.valueOf(userSeq), deviceId);
        String refreshToken = generateRefreshToken(String.valueOf(userSeq), deviceId);

        return new AuthToken(accessToken, refreshToken);
    }

    public String generateAccessToken(String subject, String audience) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + ACCESS_EXP);

        return Jwts.builder()
                .setSubject(subject)
                .setAudience(audience)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String generateRefreshToken(String subject, String audience) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + REFRESH_EXP);

        return Jwts.builder()
                .setSubject(subject)
                .setAudience(audience)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // JWT 토큰에서 사용자 정보 추출
    public Long extractUserSeq(String token) {
        return Long.valueOf(extractClaims(token).getSubject());
    }

    // JWT 토큰에서 디바이스 ID 정보 추출
    public String extractDeviceId(String token) {
        return extractClaims(token).getAudience();
    }

    // JWT 토큰의 만료 여부 확인
    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    // JWT 토큰 검증
    public boolean isValidToken(String token) {
        return token != null && !isTokenExpired(token);
    }

    private Claims extractClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    // 헤더에서 토큰 추출
    public String extractTokenFromHeader(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            return header.replace("Bearer ", "");
        }
        return null;
    }
}