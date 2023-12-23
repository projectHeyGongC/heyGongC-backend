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
        return new Token(token.getAccess_token(), token.getRefresh_token());
    }

    public User getGoogleUserInfo(Token token) throws JsonProcessingException {
        ResponseEntity<String> userInfoResponse = googleOAuth.requestUserInfo(token.access_token());
        GoogleUserResponse googleUser = googleOAuth.getUserInfo(userInfoResponse);
        return userRepository.findByEmail(googleUser.getEmail())
                .orElse(User.builder()
                        .email(googleUser.getEmail())
                        .id(googleUser.getEmail()) //추후 변경 필요(ID값 어떻게 처리할지?)
                        .sns_type(UserSnsType.GOOGLE)
                        .refresh_token(token.refresh_token())
                        .refresh_create("")
                        .refresh_expire("")
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
                        .device_id(request.device_id())
                        .email(user.getEmail())
                        .id(user.getId())
                        .sns_type(user.getSns_type())
                        .alarm(true)
                        .ads(request.ads())
                        .refresh_token(request.token().refresh_token())
                        .refresh_create("")
                        .refresh_expire("")
                        .build()
        );
        return true;
    }
}
