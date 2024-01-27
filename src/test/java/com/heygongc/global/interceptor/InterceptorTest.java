package com.heygongc.global.interceptor;

import com.heygongc.user.application.JwtUtil;
import com.heygongc.user.application.UserSnsType;
import com.heygongc.user.domain.User;
import com.heygongc.user.domain.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(MockitoExtension.class)
class InterceptorTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    private User user;
    HttpServletRequest request;
    HttpServletResponse response;
    Object handler;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .seq(1L)
                .deviceId("mockDeviceId")
                .id("mockId")
                .email("mockEmail")
                .snsType(UserSnsType.GOOGLE)
                .ads(true)
                .build();
    }

    @Test
    public void 비로그인() {
        // given
        // when
        // then
    }

    @Test
    public void 로그인_유효하지않은토큰_실패() {
        // given
        // when
        // then
    }

    @Test
    public void 로그인_새로운로그인_실패() {
        // given
        // when
        // then
    }

    @Test
    public void 로그인_성공() {
        // given
        // when
        // then
    }
}