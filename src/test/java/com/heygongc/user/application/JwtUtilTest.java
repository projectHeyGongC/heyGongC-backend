package com.heygongc.user.application;

import com.heygongc.global.error.exception.ExpiredTokenException;
import com.heygongc.global.error.exception.InvalidTokenException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    private static Long userSeq;
    private static String deviceId;

    @BeforeAll
    public static void setUpBeforeAll() {
        userSeq = 1L;
        deviceId = "mockDevicveId";
    }

    @Test
    @DisplayName("userSeq와 deviceId를 통해 유효한 JWT 토큰을 생성한다")
    public void generateAccessToken() {
        String accessToken = jwtUtil.generateUserAccessToken(String.valueOf(userSeq), deviceId);

        assertNotNull(accessToken);
        assertFalse(accessToken.isEmpty());
    }

    @Test
    @DisplayName("올바른 토큰 정보로 userSeq와 deviceId를 추출한다")
    void extractInfo() {
        String accessToken = jwtUtil.generateUserAccessToken(String.valueOf(userSeq), deviceId);

        Long userSeq = Long.parseLong(jwtUtil.extractSubject(accessToken));
        String deviceId = jwtUtil.extractAudience(accessToken);

        assertEquals(userSeq, JwtUtilTest.userSeq);
        assertEquals(deviceId, JwtUtilTest.deviceId);
    }

    @Test
    @DisplayName("유효하지 않은 토큰인 경우 예외를 리턴한다")
    void isValidWithInvalidToken() {
        String invalidToken = "invalid-token";

        assertThrows(InvalidTokenException.class, () -> jwtUtil.checkedValidTokenOrThrowException(invalidToken));
    }

    @Test
    @DisplayName("만료된 토큰으로 Valid 체크할 경우 예외를 리턴한다")
    void isValidTokenWithExpiredToken() {
        long accessExp = 1L;
        long refreshExp = 1L;
        long longAccessExp = 1L;
        JwtUtil jwtUtil = new JwtUtil("secret-key", accessExp, refreshExp, longAccessExp);

        String expiredToken = jwtUtil.generateUserAccessToken(String.valueOf(userSeq), deviceId);
        try {
            Thread.sleep(accessExp);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertThrows(ExpiredTokenException.class, () -> jwtUtil.checkedValidTokenOrThrowException(expiredToken));
    }

    @Test
    @DisplayName("만료된 토큰으로 사용자 정보를 추출할 경우 예외를 리턴한다")
    void extractInfoWithExpiredToken() {
        long accessExp = 1L;
        long refreshExp = 1L;
        long longAccessExp = 1L;
        JwtUtil jwtUtil = new JwtUtil("secret-key", accessExp, refreshExp, longAccessExp);

        String expiredToken = jwtUtil.generateUserAccessToken(String.valueOf(userSeq), deviceId);
        try {
            Thread.sleep(accessExp);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertThrows(ExpiredTokenException.class, () -> jwtUtil.extractSubject(expiredToken));
    }

    @Test
    @DisplayName("시크릿 키가 틀린 토큰 정보로 사용자 정보를 추출할 경우 예외를 리턴한다")
    void extractInfoWithWrongSecretKey() {
        String correctSecretKey = "correct-secret-key";
        String wrongSecretKey = "wrong-secret-key";

        JwtUtil jwtUtil = new JwtUtil(correctSecretKey, 3600000L, 3600000L, 3600000L);
        String token = jwtUtil.generateUserAccessToken(String.valueOf(userSeq), deviceId);

        JwtUtil wrongJwtUtil = new JwtUtil(wrongSecretKey, 3600000L, 3600000L, 3600000L);
        assertThrows(InvalidTokenException.class, () -> wrongJwtUtil.extractSubject(token));
    }
}