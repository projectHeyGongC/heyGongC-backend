package com.heygongc.global.config;

import com.heygongc.global.interceptor.Interceptor;
import com.heygongc.user.application.JwtUtil;
import com.heygongc.user.domain.UserRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public WebMvcConfig(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new Interceptor(jwtUtil, userRepository))
                .addPathPatterns("/**");
    }
}
