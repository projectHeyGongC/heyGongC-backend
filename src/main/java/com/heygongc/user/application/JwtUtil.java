package com.heygongc.user.application;

import com.heygongc.device.exception.DeviceExpiredTokenException;
import com.heygongc.device.exception.InvalidDeviceTokenException;
import com.heygongc.user.exception.UserExpiredTokenException;
import com.heygongc.user.exception.InvalidUserTokenException;
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
    private final Long LONG_ACCESS_EXP;
    private final JwtParser jwtParser;

    public JwtUtil(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access.expiration}") Long accessExp,
            @Value("${jwt.refresh.expiration}") Long refreshExp,
            @Value("${jwt.long.access.expiration}") Long longAccessExp
    ) {
        this.SECRET_KEY = secretKey;
        this.ACCESS_EXP = accessExp;
        this.REFRESH_EXP = refreshExp;
        this.LONG_ACCESS_EXP = longAccessExp;
        this.jwtParser = Jwts.parser().setSigningKey(SECRET_KEY);
    }

    public String generateUserAccessToken(String subject, String audience) {
        return genarateToken("USER", subject, audience, ACCESS_EXP);
    }

    public String generateUserRefreshToken(String subject, String audience) {
        return genarateToken("USER", subject, audience, REFRESH_EXP);
    }

    public String generateCameraAccessToken(String subject, String audience) {
        return genarateToken("CAMERA", subject, audience, LONG_ACCESS_EXP);
    }

    public String genarateToken(String issuer, String subject, String audience, Long exp) {

        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + exp);

        return Jwts.builder()
                .setIssuer(issuer)
                .setSubject(subject)
                .setAudience(audience)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String extractSubject(String token) {
        return extractClaims(token).getSubject();
    }

    public String extractIssuers(String token) {
        return extractClaims(token).getIssuer();
    }

    public String extractAudience(String token) {
        return extractClaims(token).getAudience();
    }

    // USER JWT 토큰이 정상인지 체크, 오류일 경우 Exception 발생
    public void UserCheckedValidTokenOrThrowException(String token) {
        try {
            jwtParser.parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new UserExpiredTokenException();
        } catch (JwtException e) {
            throw new InvalidUserTokenException();
        }
    }

    // DEVICE JWT 토큰이 정상인지 체크, 오류일 경우 Exception 발생

    public void DeviceCheckedValidTokenOrThrowException(String token){

        try {
            jwtParser.parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new DeviceExpiredTokenException();
        } catch (JwtException e) {
            throw new InvalidDeviceTokenException();
        }


    }

    public Claims extractClaims(String token) {
        try {
            return jwtParser.parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            throw new UserExpiredTokenException();
        } catch (JwtException e) {
            throw new InvalidUserTokenException();
        }
    }
}