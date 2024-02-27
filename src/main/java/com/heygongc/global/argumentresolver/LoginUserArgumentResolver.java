package com.heygongc.global.argumentresolver;

import com.heygongc.global.error.exception.ForbiddenException;
import com.heygongc.global.error.exception.UnauthenticatedException;
import com.heygongc.user.application.JwtUtil;
import com.heygongc.user.domain.User;
import com.heygongc.user.domain.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public LoginUserArgumentResolver(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasLoginUserAnnotation = parameter.hasParameterAnnotation(LoginUser.class);
        boolean isUserType = User.class.isAssignableFrom(parameter.getParameterType());
        return hasLoginUserAnnotation && isUserType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
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

        return user.get();

    }
}
