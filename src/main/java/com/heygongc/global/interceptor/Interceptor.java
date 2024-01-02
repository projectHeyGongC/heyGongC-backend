package com.heygongc.global.interceptor;

import com.heygongc.user.application.JwtUtil;
import com.heygongc.user.domain.User;
import com.heygongc.user.domain.UserRepository;
import com.heygongc.user.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        logger.debug(">>>>>>>>>>>>>>>>>>>> BEGIN >>>>>>>>>>>>>>>>>>>>");
        logger.debug("{} >>>>> {}", request.getRequestURI(), "");

        // 로그인 관련 페이지는 무시. 회원탈퇴는 예외로 request.setAttribute가 필요해서 제외
        if (request.getRequestURI().startsWith("/v1/user/")
            && !request.getRequestURI().equals("/v1/user/unRegister")) {
            return true;
        }

        String accessToken = extractTokenFromHeader(request.getHeader("access-token"));
        String refreshToken = extractTokenFromHeader(request.getHeader("refresh-token"));

        // 유효하지 않은 토큰이면 로그인 페이지로 리디렉션
        if (!isValidToken(accessToken) || !isValidToken(refreshToken)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            return false;
        }

        String userSeq = jwtUtil.extractUsername(accessToken);
        String deviceId = jwtUtil.extractAudience(accessToken);
        logger.debug("userSeq({}), deviceId({})",userSeq, deviceId);

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
        logger.debug("{} ({}ms) >>>>> {}", request.getRequestURI(), (System.currentTimeMillis() - startTime), response.getStatus());
        logger.debug("<<<<<<<<<<<<<<<<<<<<< END <<<<<<<<<<<<<<<<<<<<<");
        logger.debug("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
    }
}
