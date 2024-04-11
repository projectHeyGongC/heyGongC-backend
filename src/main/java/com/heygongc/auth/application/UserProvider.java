package com.heygongc.auth.application;

import com.heygongc.global.error.exception.ForbiddenException;
import com.heygongc.global.error.exception.UnauthenticatedException;
import com.heygongc.user.domain.entity.User;
import com.heygongc.user.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Qualifier("userProvider")
@Component
public class UserProvider implements TokenProvider {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public UserProvider(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    public Object extract(String accessToken) {
        Long userSeq = Long.parseLong(jwtUtil.extractSubject(accessToken));
        String deviceId = jwtUtil.extractAudience(accessToken);

        User user = userRepository.findById(userSeq)
                .orElseThrow(UnauthenticatedException::new);

        if (!Objects.equals(user.getDeviceId(), deviceId)) {
            throw new ForbiddenException("새로운 로그인이 감지되었습니다.");
        }

        return user;
    }
}
