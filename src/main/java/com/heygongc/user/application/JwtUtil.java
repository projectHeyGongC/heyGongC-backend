package com.heygongc.user.application;

import com.heygongc.user.exception.ExpiredTokenException;
import com.heygongc.user.exception.InvalidTokenException;
import io.jsonwebtoken.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Getter
public class JwtUtil {

    private final String SECRET_KEY;
    private final Long ACCESS_EXP;
    private final Long REFRESH_EXP;
    private final JwtParser jwtParser;

    public JwtUtil(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access.expiration}") Long accessExp,
            @Value("${jwt.refresh.expiration}") Long refreshExp
    ) {
        this.SECRET_KEY = secretKey;
        this.ACCESS_EXP = accessExp;
        this.REFRESH_EXP = refreshExp;
        this.jwtParser = Jwts.parser().setSigningKey(SECRET_KEY);
    }

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
    public String extractSubject(String token) {
        return extractClaims(token).getSubject();
    }

    // JWT 토큰에서 디바이스 ID 정보 추출
    public String extractAudience(String token) {
        return extractClaims(token).getAudience();
    }

    // JWT 토큰이 정상인지 체크, 오류일 경우 Exception 발생
    public void checkedValidTokenOrThrowException(String token) {
        try {
            jwtParser.parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException();
        } catch (JwtException e) {
            throw new InvalidTokenException();
        }
    }

    public Claims extractClaims(String token) {
        try {
            return jwtParser.parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException();
        } catch (JwtException e) {
            throw new InvalidTokenException();
        }
    }
}