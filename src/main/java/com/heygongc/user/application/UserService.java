package com.heygongc.user.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.heygongc.global.config.EmailSigninFailedException;
import com.heygongc.user.domain.User;
import com.heygongc.user.domain.UserRepository;
import com.heygongc.user.presentation.request.UserCreateRequest;
import com.heygongc.user.presentation.request.UserLoginRequest;
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

    public String getGoogleLoginUrl() {
        return googleOAuth.getGoogleLoginUrl();
    }

    public Token getGoogleToken(String authCode) throws JsonProcessingException {
        ResponseEntity<String> accessTokenResponse = googleOAuth.requestAccessToken(authCode);
        GoogleTokenResponse token = googleOAuth.getAccessToken(accessTokenResponse);
        return new Token(token.accessToken(), token.refreshToken());
    }

    public User getGoogleUserInfo(Token token) throws JsonProcessingException {
        ResponseEntity<String> userInfoResponse = googleOAuth.requestUserInfo(token.accessToken());
        GoogleUserResponse googleUser = googleOAuth.getUserInfo(userInfoResponse);
        return userRepository.findByEmail(googleUser.email())
                .orElse(User.builder()
                        .email(googleUser.email())
                        .id(googleUser.email()) //추후 변경 필요(ID값 어떻게 처리할지?)
                        .snsType(UserSnsType.GOOGLE)
                        .refreshToken(token.refreshToken())
                        .refreshCreate(null)
                        .refreshExpire(null)
                        .build()
                );
    }

    public Boolean isUserExists(User user) {
        return user.getSeq() != null;
    }

    public Boolean isUserExists(UserLoginRequest request) throws JsonProcessingException {
        User user = getGoogleUserInfo(request.token());
        return isUserExists(user);
    }

    public Boolean loginUser(UserLoginRequest request) throws JsonProcessingException {
        User user = getGoogleUserInfo(request.token());
        return isUserExists(user);
    }

    @Transactional
    public Boolean createUser(UserCreateRequest request) throws JsonProcessingException {
        User user = getGoogleUserInfo(request.token());
        if(isUserExists(user)) {
            throw new EmailSigninFailedException("이미 가입한 사용자입니다.");
        }

        userRepository.save(
                User.builder()
                        .deviceId(request.deviceId())
                        .id(user.getId())
                        .snsType(user.getSnsType())
                        .email(user.getEmail())
                        .alarm(true)
                        .ads(request.ads())
                        .refreshToken(request.token().refreshToken())
                        .refreshCreate(null)
                        .refreshExpire(null)
                        .build()
        );
        return true;
    }
}
