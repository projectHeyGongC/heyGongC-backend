package com.heygongc.global.config;

import com.heygongc.global.argumentresolver.LoginUserArgumentResolver;
import com.heygongc.global.interceptor.Interceptor;
import com.heygongc.user.application.JwtUtil;
import com.heygongc.user.domain.UserRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final LoginUserArgumentResolver loginUserArgumentResolver;

    public WebMvcConfig(JwtUtil jwtUtil, UserRepository userRepository, LoginUserArgumentResolver loginUserArgumentResolver) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.loginUserArgumentResolver = loginUserArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
        resolvers.add(loginUserArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new Interceptor(jwtUtil, userRepository))
                .addPathPatterns("/**");
    }
}
