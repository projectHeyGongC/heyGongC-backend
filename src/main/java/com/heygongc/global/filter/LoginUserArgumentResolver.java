package com.heygongc.global.filter;

import com.heygongc.global.error.exception.ForbiddenException;
import com.heygongc.global.error.exception.UnauthenticatedException;
import com.heygongc.user.application.JwtUtil;
import com.heygongc.user.domain.entity.User;
import com.heygongc.user.domain.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;

@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String AUTHORIZATION = "Authorization";
    private static final String Bearer = "Bearer ";

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public LoginUserArgumentResolver(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType() == User.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String authorization = webRequest.getHeader(AUTHORIZATION);
        String accessToken = extractTokenFromHeader(authorization);
        if (accessToken == null) {
            throw new UnauthenticatedException();
        }

        // 유효하지 않은 토큰이면 로그인 페이지로 리디렉션
        jwtUtil.checkedValidTokenOrThrowException(accessToken);

        Long userSeq = Long.parseLong(jwtUtil.extractSubject(accessToken));
        String deviceId = jwtUtil.extractAudience(accessToken);
        logger.info("userSeq({}), deviceId({})", userSeq, deviceId);

        User user = userRepository.findById(userSeq)
                .orElseThrow(UnauthenticatedException::new);

        if (!Objects.equals(user.getDeviceId(), deviceId)) {
            throw new ForbiddenException("새로운 로그인이 감지되었습니다.");
        }

        return user;
    }

    private String extractTokenFromHeader(String header) {
        if (header != null && header.startsWith(Bearer)) {
            return header.replace(Bearer, "");
        }
        return null;
    }
}
