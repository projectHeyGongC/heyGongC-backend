package com.heygongc.user.application;

import com.heygongc.user.domain.User;
import com.heygongc.user.domain.UserRepository;
import com.heygongc.user.domain.UserToken;
import com.heygongc.user.domain.UserTokenRepository;
import com.heygongc.user.exception.EmailSigninFailedException;
import com.heygongc.user.presentation.request.UserLoginRequest;
import com.heygongc.user.presentation.request.UserRegisterRequest;
import com.heygongc.user.presentation.response.GoogleTokenResponse;
import com.heygongc.user.presentation.response.GoogleUserResponse;
import com.heygongc.user.presentation.response.TokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final GoogleOAuth googleOAuth;
    private final UserRepository userRepository;
    private final UserTokenRepository userTokenRepository;
    private final JwtUtil jwtUtil;

    public UserService(GoogleOAuth googleOAuth, UserRepository userRepository, UserTokenRepository userTokenRepository, JwtUtil jwtUtil) {
        this.googleOAuth = googleOAuth;
        this.userRepository = userRepository;
        this.userTokenRepository = userTokenRepository;
        this.jwtUtil = jwtUtil;
    }

    public String getGoogleLoginUrl() {
        return googleOAuth.getLoginUrl();
    }

    public TokenResponse getGoogleToken(String code) {
        GoogleTokenResponse token = googleOAuth.getToken(code);
        return new TokenResponse(token.accessToken(), token.refreshToken());
    }

    public User getGoogleUserInfo(String token) {
        GoogleUserResponse googleUser = googleOAuth.getUser(token);
        return userRepository.findBySnsId(googleUser.sub())
                .orElse(User.builder()
                        .snsId(googleUser.sub())
                        .email(googleUser.email())
                        .build());
    }

    @Transactional
    public TokenResponse googleLogin(UserLoginRequest request) {
        User user = getGoogleUserInfo(request.token().accessToken());
        // 신규 가입인 경우
        if (!isUserExists(user)) {
            return null;
        }
        
        // device 정보 저장
        user.setDeviceId(request.deviceId());
        user.setDeviceOs(request.deviceOs());
        userRepository.save(user);

        // jwt 토큰 발급
        TokenResponse tokenResponse = new TokenResponse(
                jwtUtil.generateAccessToken(String.valueOf(user.getSeq()), request.deviceId()),
                jwtUtil.generateRefreshToken(String.valueOf(user.getSeq()), request.deviceId())
        );

        // 이미 등록된 jwt 토큰이 있으면 삭제
        if (userTokenRepository.findByUserSeq(user.getSeq()).isPresent()) {
            userTokenRepository.deleteByUserSeq(user.getSeq());
        }

        // jwt 토큰 저장
        UserToken userToken = new UserToken(tokenResponse.refreshToken(), user);
        userTokenRepository.save(userToken);

        return tokenResponse;
    }

    @Transactional
    public TokenResponse googleRegister(UserRegisterRequest request) {
        User googleUser = getGoogleUserInfo(request.token().accessToken());
        // 이미 가입된 경우
        if (isUserExists(googleUser)) {
            throw new EmailSigninFailedException("이미 가입한 사용자입니다.");
        }

        // 회원가입
        User user = userRepository.save(
                User.builder()
                        .deviceId(request.deviceId())
                        .deviceOs(request.deviceOs())
                        .id("USER" + ((int) (Math.random() * 9999) + 1)) // USER + 랜덤4자리
                        .snsType(UserSnsType.GOOGLE)
                        .snsId(googleUser.getSnsId())
                        .email(googleUser.getEmail())
                        .alarm(true)
                        .ads(request.ads())
                        .build()
        );

        // jwt 토큰 발급
        TokenResponse tokenResponse = new TokenResponse(
                jwtUtil.generateAccessToken(String.valueOf(user.getSeq()), request.deviceId()),
                jwtUtil.generateRefreshToken(String.valueOf(user.getSeq()), request.deviceId())
        );

        // jwt 토큰 저장
        UserToken userToken = new UserToken(tokenResponse.refreshToken(), user);
        userTokenRepository.save(userToken);

        return tokenResponse;
    }

    public Boolean isUserExists(User user) {
        return user != null && user.getSeq() != null;
    }
}
