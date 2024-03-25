package com.heygongc.global.config;

import com.heygongc.global.filter.DeviceArgumentResolver;
import com.heygongc.global.filter.LoginUserArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final LoginUserArgumentResolver loginUserArgumentResolver;
    private final DeviceArgumentResolver deviceArgumentResolver;

    public WebMvcConfig(LoginUserArgumentResolver loginUserArgumentResolver, DeviceArgumentResolver deviceArgumentResolver) {
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
