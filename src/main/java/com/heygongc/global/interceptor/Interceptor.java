package com.heygongc.global.interceptor;

import com.heygongc.user.application.JwtUtil;
import com.heygongc.user.domain.User;
import com.heygongc.user.domain.UserRepository;
import com.heygongc.user.exception.UserNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

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

        Long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        logger.info(">>>>>>>>>>>>>>>>>>>> BEGIN >>>>>>>>>>>>>>>>>>>>");
        logger.info("{} >>>>> {}", request.getRequestURI(), "");

        // 비로그인 컨트롤러는 정상처리
        if (!isAuthController(handler)) {
            return true;
        }

        // 로그인 컨트롤러는 jwt 토큰 검증
        String accessToken = extractTokenFromHeader(request.getHeader("access-token"));
        String refreshToken = extractTokenFromHeader(request.getHeader("refresh-token"));

        // 유효하지 않은 토큰이면 로그인 페이지로 리디렉션
        try {
            if (!isValidToken(accessToken) || !isValidToken(refreshToken)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
                return false;
            }
        } catch (SignatureException | MalformedJwtException e) { //서명 오류 or JWT 구조 문제
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            return false;
        } catch (ExpiredJwtException e) { //유효 기간 만료
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            return false;
        } catch(Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            return false;
        }

        String userSeq = jwtUtil.extractUsername(accessToken);
        String deviceId = jwtUtil.extractAudience(accessToken);
        logger.info("userSeq({}), deviceId({})",userSeq, deviceId);

        // DB의 deviceId와 토큰값이 다를 경우
        User user = userRepository.findById(Long.valueOf(userSeq)).orElseThrow(UserNotFoundException::new);
        if (!deviceId.equals(user.getDeviceId())) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
            return false;
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

    private boolean isValidToken(String jwtToken) {
        return jwtToken != null && jwtUtil.validateToken(jwtToken);
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        Long startTime = (Long) request.getAttribute("startTime");
        logger.info("{} ({}ms) >>>>> {}", request.getRequestURI(), (System.currentTimeMillis() - startTime), response.getStatus());
        logger.info("<<<<<<<<<<<<<<<<<<<<< END <<<<<<<<<<<<<<<<<<<<<");
        logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
    }
}
