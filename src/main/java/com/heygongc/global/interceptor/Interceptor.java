package com.heygongc.global.interceptor;

import com.heygongc.global.error.exception.ForbiddenException;
import com.heygongc.user.application.JwtUtil;
import com.heygongc.user.domain.entity.User;
import com.heygongc.user.domain.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

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
        if (!isLoginController(handler)) {
            return true;
        }

        // 로그인 컨트롤러는 jwt 토큰 검증
        String accessToken = jwtUtil.extractTokenFromHeader(request.getHeader("Authorization"));

        // 유효하지 않은 토큰이면 로그인 페이지로 리디렉션
        jwtUtil.isValidTokenOrThrowException(accessToken);

        Long userSeq = jwtUtil.extractUserSeq(accessToken);
        String deviceId = jwtUtil.extractDeviceId(accessToken);
        logger.info("userSeq({}), deviceId({})", userSeq, deviceId);

        // DB에 저장되어 있지 않을 경우
        Optional<User> user = userRepository.findById(userSeq);
        if (user.isEmpty()) {
            throw new ForbiddenException("사용자를 찾을 수 없습니다.");
        }

        // DB의 deviceId와 토큰값이 다를 경우
        if (!deviceId.equals(user.get().getDeviceId())) {
            throw new ForbiddenException("새로운 로그인이 감지되었습니다.");
        }

        return true;
    }

    private boolean isLoginController(Object handler) {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return false;
        }

        IsLogin isLogin = handlerMethod.getMethodAnnotation(IsLogin.class);
        return isLogin != null;
    }
}
