package com.heygongc.global.filter;

import com.heygongc.device.domain.entity.Device;
import com.heygongc.device.domain.repository.DeviceRepository;
import com.heygongc.device.exception.DeviceNotFoundException;
import com.heygongc.global.error.exception.ForbiddenException;
import com.heygongc.global.error.exception.UnauthenticatedException;
import com.heygongc.user.application.JwtUtil;
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
public class PairDeviceArgumentResolver implements HandlerMethodArgumentResolver {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String AUTHORIZATION = "Authorization";
    private static final String Bearer = "Bearer ";

    private final JwtUtil jwtUtil;
    private final DeviceRepository deviceRepository;


    public PairDeviceArgumentResolver(JwtUtil jwtUtil, DeviceRepository deviceRepository) {
        this.jwtUtil = jwtUtil;
        this.deviceRepository = deviceRepository;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType() == Device.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String authorization = webRequest.getHeader(AUTHORIZATION);
        String accessToken = extractTokenFromHeader(authorization);
        if (accessToken == null) {
            throw new UnauthenticatedException();
        }

        // 유효하지 않은 토큰이면 로그인 페이지로 리디렉션
        jwtUtil.DeviceCheckedValidTokenOrThrowException(accessToken);

        Long deviceSeq = Long.parseLong(jwtUtil.extractSubject(accessToken));
        String deviceId = jwtUtil.extractAudience(accessToken);
        logger.info("deviceSeq({}), deviceId({})", deviceSeq, deviceId);

        Device device = deviceRepository.findByDeviceId(deviceId)
                .orElseThrow(DeviceNotFoundException::new);

        if (!Objects.equals(device.getDeviceId(), deviceId)) {
            throw new ForbiddenException("서로 다른 기기입니다.");
        }

        return device;
    }

    private String extractTokenFromHeader(String header) {
        if (header != null && header.startsWith(Bearer)) {
            return header.replace(Bearer, "");
        }
        return null;
    }
}

