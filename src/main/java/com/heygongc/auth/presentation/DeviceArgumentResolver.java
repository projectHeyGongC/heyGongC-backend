package com.heygongc.auth.presentation;

import com.heygongc.auth.application.TokenProvider;
import com.heygongc.device.domain.entity.Device;
import com.heygongc.global.error.exception.UnauthenticatedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class DeviceArgumentResolver implements HandlerMethodArgumentResolver {
    private static final String AUTHORIZATION = "Authorization";
    private static final String Bearer = "Bearer ";

    private final TokenProvider tokenProvider;

    public DeviceArgumentResolver(@Qualifier("deviceProvider") TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
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

        return tokenProvider.extract(accessToken);
    }

    private String extractTokenFromHeader(String header) {
        if (header != null && header.startsWith(Bearer)) {
            return header.replace(Bearer, "");
        }
        return null;
    }
}

