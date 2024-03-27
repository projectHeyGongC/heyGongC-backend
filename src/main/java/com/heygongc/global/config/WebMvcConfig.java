package com.heygongc.global.config;

import com.heygongc.global.filter.LoginUserArgumentResolver;
import com.heygongc.global.filter.PairDeviceArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final LoginUserArgumentResolver loginUserArgumentResolver;
    private final PairDeviceArgumentResolver deviceArgumentResolver;

    public WebMvcConfig(LoginUserArgumentResolver loginUserArgumentResolver, PairDeviceArgumentResolver deviceArgumentResolver) {
        this.loginUserArgumentResolver = loginUserArgumentResolver;
        this.deviceArgumentResolver = deviceArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
        resolvers.add(loginUserArgumentResolver);
        resolvers.add(deviceArgumentResolver);
    }
}
