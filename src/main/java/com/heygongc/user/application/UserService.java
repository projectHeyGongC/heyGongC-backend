package com.heygongc.user.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.heygongc.global.config.EmailSigninFailedException;
import com.heygongc.user.domain.GoogleOAuth;
import com.heygongc.user.domain.User;
import com.heygongc.user.domain.UserRepository;
import com.heygongc.user.presentation.request.UserCreateRequest;
import com.heygongc.user.presentation.request.UserLoginRequest;
import com.heygongc.user.presentation.request.UserSnsType;
import com.heygongc.user.presentation.response.GoogleTokenResponse;
import com.heygongc.user.presentation.response.GoogleUserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final GoogleOAuth googleOAuth;
    private final UserRepository userRepository;

    public UserService(GoogleOAuth googleOAuth, UserRepository userRepository) {
        this.googleOAuth = googleOAuth;
        this.userRepository = userRepository;
    }

    public User getGoogleUserInfo(String authCode) throws JsonProcessingException {
        ResponseEntity<String> accessTokenResponse = googleOAuth.requestAccessToken(authCode);
        GoogleTokenResponse token = googleOAuth.getAccessToken(accessTokenResponse);
        ResponseEntity<String> userInfoResponse = googleOAuth.requestUserInfo(token);
        GoogleUserResponse googleUser = googleOAuth.getUserInfo(userInfoResponse);
        return userRepository.findByEmail(googleUser.getEmail())
                .orElse(User.builder()
                        .email(googleUser.getEmail())
                        .id(googleUser.getEmail()) //추후 변경 필요(ID값 어떻게 처리할지?)
                        .sns_type(UserSnsType.GOOGLE)
                        .access_token(token.getAccess_token())
                        .refresh_token(token.getRefresh_token())
                        .build()
                );
    }

    public Boolean isUserExists(UserLoginRequest request) throws JsonProcessingException {
        if(request.seq() == null) return false;
        return userRepository.existsById(request.seq());
    }

    @Transactional
    public User loginUser(UserLoginRequest request) throws JsonProcessingException {
        if(!isUserExists(request)) { throw new EmailSigninFailedException(); }

        return userRepository.findById(request.seq()).orElseThrow(EmailSigninFailedException::new);
    }

    @Transactional
    public User createUser(UserCreateRequest request) {
        if(userRepository.existsByEmail(request.email())) { throw new EmailSigninFailedException(); }
        return userRepository.save(
                User.builder()
                        .device_id(request.device_id())
                        .email(request.email())
                        .id(request.id())
                        .sns_type(request.sns_type())
                        .alarm(true)
                        .ads(request.ads())
                        .access_token(request.access_token())
                        .refresh_token(request.refresh_token())
                        .build()
        );
    }
}
