package com.heygongc.global.argumentresolver;

import com.heygongc.user.application.JwtUtil;
import com.heygongc.user.domain.User;
import com.heygongc.user.domain.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

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
        try {
            if (!jwtUtil.isValidToken(accessToken)) {
                return null;
            }
        } catch (SignatureException | MalformedJwtException e) { //서명 오류 or JWT 구조 문제
            return null;
        } catch (ExpiredJwtException e) { //유효 기간 만료
            return null;
        } catch(Exception e) {
            return null;
        }

        Long userSeq = jwtUtil.extractUserSeq(accessToken);
        String deviceId = jwtUtil.extractDeviceId(accessToken);

        User user = userRepository.findById(userSeq).orElse(null);

        return user;

    }
}
