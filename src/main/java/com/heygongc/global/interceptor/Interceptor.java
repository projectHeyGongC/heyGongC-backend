package com.heygongc.global.interceptor;

import com.heygongc.global.error.exception.ForbiddenException;
import com.heygongc.global.error.exception.UnauthenticatedException;
import com.heygongc.user.application.JwtUtil;
import com.heygongc.user.domain.User;
import com.heygongc.user.domain.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

/**
 * TOBE preHandle에서 RequestParams, postHandle에서 ResponseEntity 추가
 */
public class Interceptor implements HandlerInterceptor {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public Interceptor(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Controller 실행 전

        // 비로그인 컨트롤러는 정상처리
        if (!isAuthController(handler)) {
            return true;
        }

        // 로그인 컨트롤러는 jwt 토큰 검증
        String accessToken = extractTokenFromHeader(request.getHeader("access-token"));
        String refreshToken = extractTokenFromHeader(request.getHeader("refresh-token"));

        // 유효하지 않은 토큰이면 로그인 페이지로 리디렉션
        try {
            if (!jwtUtil.isValidToken(accessToken) || !jwtUtil.isValidToken(refreshToken)) {
                throw new UnauthenticatedException("유효하지 않은 토큰입니다.");
            }
        } catch (SignatureException | MalformedJwtException e) { //서명 오류 or JWT 구조 문제
            throw new UnauthenticatedException("유효하지 않은 토큰입니다.");
        } catch (ExpiredJwtException e) { //유효 기간 만료
            throw new UnauthenticatedException("유효하지 않은 토큰입니다.");
        } catch(Exception e) {
            throw new UnauthenticatedException("유효하지 않은 토큰입니다.");
        }

        Long userSeq = jwtUtil.extractUserSeq(accessToken);
        String deviceId = jwtUtil.extractDeviceId(accessToken);
        logger.info("userSeq({}), deviceId({})", userSeq, deviceId);

        // DB에 저장되어 있지 않을 경우
        Optional<User> userOptional = userRepository.findById(userSeq);
        if (userOptional.isEmpty()) {
            throw new ForbiddenException("사용자를 찾을 수 없습니다.");
        }

        // DB의 deviceId와 토큰값이 다를 경우
        User user = userOptional.get();
        if (!deviceId.equals(user.getDeviceId())) {
            throw new ForbiddenException("새로운 로그인이 감지되었습니다.");
        }

        request.setAttribute("userSeq", userSeq);
        request.setAttribute("deviceId", deviceId);
        return true;
    }

    private boolean isAuthController(Object handler) {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return false;
        }

        Auth auth = handlerMethod.getMethodAnnotation(Auth.class);
        return auth != null;
    }

    private String extractTokenFromHeader(String header) {
        // 헤더에서 토큰 추출
        if (header != null && header.startsWith("Bearer ")) {
            return header.replace("Bearer ", "");
        }
        return null;
    }
}
