package com.heygongc.user.application.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.heygongc.global.config.EmailSigninFailedException;
import com.heygongc.user.application.domain.User;
import com.heygongc.user.application.domain.UserRepository;
import com.heygongc.user.application.presentation.response.GoogleUserResponse;
import com.heygongc.user.application.domain.GoogleOAuth;
import com.heygongc.user.application.presentation.request.UserCreateRequest;
import com.heygongc.user.application.presentation.request.UserSnsType;
import com.heygongc.user.application.presentation.response.GoogleTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
public class UserService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final GoogleOAuth googleOAuth;
    private final UserRepository userRepository;

    public UserService(GoogleOAuth googleOAuth, UserRepository userRepository) {
        this.googleOAuth = googleOAuth;
        this.userRepository = userRepository;
    }

    public User googleLogin(String authCode) throws IOException {
        GoogleUserResponse googleUser = getGoogleUserInfo(authCode);

        if(!userRepository.existsByEmail(googleUser.getEmail())) {
            userRepository.save(
                    User.builder()
                            .email(googleUser.getEmail())
                            .id(googleUser.getName())
                            .sns_type(UserSnsType.GOOGLE)
                            .alarm(true)
                            .ads(true)
                            .build()
            );
        }
        return userRepository.findByEmail(
                googleUser.getEmail()
        ).orElseThrow(EmailSigninFailedException::new);
    }

    public GoogleUserResponse getGoogleUserInfo(String authCode) throws JsonProcessingException {
        ResponseEntity<String> accessTokenResponse = googleOAuth.requestAccessToken(authCode);
        GoogleTokenResponse token = googleOAuth.getAccessToken(accessTokenResponse);
        ResponseEntity<String> userInfoResponse = googleOAuth.requestUserInfo(token);
        GoogleUserResponse googleUser = googleOAuth.getUserInfo(userInfoResponse);
        return googleUser;
    }

    @Transactional
    public User createTestUser(UserCreateRequest request) {
        User user = new User(
                request.id(),
                request.sns_type(),
                request.email(),
                request.alarm(),
                request.ads()
        );
        return userRepository.save(user);
    }
}
