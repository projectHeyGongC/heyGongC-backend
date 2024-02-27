package com.heygongc.user.application;

import com.heygongc.user.application.apple.AppleOAuthUserProvider;
import com.heygongc.user.application.google.GoogleOAuth;
import com.heygongc.user.domain.User;
import com.heygongc.user.domain.UserRepository;
import com.heygongc.user.domain.UserToken;
import com.heygongc.user.domain.UserTokenRepository;
import com.heygongc.user.exception.*;
import com.heygongc.user.presentation.request.UserLoginRequest;
import com.heygongc.user.presentation.request.UserRegisterRequest;
import com.heygongc.user.presentation.response.OAuthUserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final GoogleOAuth googleOAuth;
    private final AppleOAuthUserProvider appleOAuthUserProvider;
    private final UserRepository userRepository;
    private final UserTokenRepository userTokenRepository;
    private final JwtUtil jwtUtil;

    public UserService(GoogleOAuth googleOAuth, AppleOAuthUserProvider appleOAuthUserProvider, UserRepository userRepository, UserTokenRepository userTokenRepository, JwtUtil jwtUtil) {
        this.googleOAuth = googleOAuth;
        this.appleOAuthUserProvider = appleOAuthUserProvider;
        this.userRepository = userRepository;
        this.userTokenRepository = userTokenRepository;
        this.jwtUtil = jwtUtil;
    }

    public Boolean isUserExists(User user) {
        return user != null && user.getSeq() != null;
    }

    public Boolean isUserUnRegistered(User user) {
        return user != null && user.getDeletedAt() != null;
    }

    public User getGoogleUserInfo(String token) {
        OAuthUserResponse googleUser = googleOAuth.getUser(token);
        return userRepository.findBySnsId(googleUser.sub())
                .orElse(User.builder()
                        .snsType(UserSnsType.GOOGLE)
                        .snsId(googleUser.sub())
                        .email(googleUser.email())
                        .build());
    }

    public User getAppleUserInfo(String token) {
        OAuthUserResponse appleUser =
                appleOAuthUserProvider.getApplePlatformMember(token);
        return userRepository.findBySnsId(appleUser.sub())
                .orElse(User.builder()
                        .snsType(UserSnsType.APPLE)
                        .snsId(appleUser.sub())
                        .email(appleUser.email())
                        .build());
    }

    private UserToken saveJwtToken(Long seq, String refreshToken) {
        // 이미 등록된 jwt 토큰이 있으면 삭제
        if (userTokenRepository.findByUserSeq(seq).isPresent()) {
            userTokenRepository.deleteToken(seq);
        }

        // jwt 토큰 저장
        UserToken userToken = new UserToken(refreshToken, User.builder().seq(seq).build());
        userTokenRepository.save(userToken);

        return userToken;
    }

    @Transactional
    public AuthToken login(String snsType, UserLoginRequest request) {
        User user = null;
        switch (snsType) {
            case "google":
                user = getGoogleUserInfo(request.token().accessToken());
                break;
            case "apple":
                user = getAppleUserInfo(request.token().accessToken());
                break;
            default:
                break;
        }

        // 신규 가입인 경우 or 이미 탈퇴한 경우(재가입)
        if (!isUserExists(user) || isUserUnRegistered(user)) {
            return null;
        }

        // device 정보 저장
        user.deviceInfo(request.deviceId(), request.deviceOs());
        userRepository.save(user);

        // jwt 토큰 발급
        AuthToken authToken = jwtUtil.generateAuthToken(user.getSeq(), user.getDeviceId());

        // 토큰 저장
        saveJwtToken(user.getSeq(), authToken.getRefreshToken());

        return authToken;
    }

    @Transactional
    public AuthToken register(String snsType, UserRegisterRequest request) {
        User user = null;
        switch (snsType) {
            case "google":
                user = getGoogleUserInfo(request.token().accessToken());
                break;
            case "apple":
                user = getAppleUserInfo(request.token().accessToken());
                break;
            default:
                break;
        }
        // 탈퇴하지 않고 가입되어 있는 경우
        if (!isUserUnRegistered(user) && isUserExists(user)) {
            throw new AlreadySignUpException("이미 가입한 사용자입니다.");
        }

        User saveUser;
        if (isUserUnRegistered(user)) {
            // 탈퇴 후 재가입
            user.reRegister(request.deviceId(), request.deviceOs(), request.ads());
            saveUser = userRepository.save(user);
        } else {
            // 최초 회원가입
            saveUser = userRepository.save(
                    User.builder()
                            .deviceId(request.deviceId())
                            .deviceOs(request.deviceOs())
                            .id("USER" + ((int) (Math.random() * 9999) + 1)) // USER + 랜덤4자리
                            .snsType(user.getSnsType())
                            .snsId(user.getSnsId())
                            .email(user.getEmail())
                            .alarm(true)
                            .ads(request.ads())
                            .build()
            );
        }

        // jwt 토큰 발급
        AuthToken authToken = jwtUtil.generateAuthToken(user.getSeq(), user.getDeviceId());

        // 토큰 저장
        saveJwtToken(user.getSeq(), authToken.getRefreshToken());

        return authToken;
    }

    @Transactional
    public void unRegister(Long userSeq) {
        User user = userRepository.findById(userSeq).orElseThrow(() -> new UserNotFoundException("미가입 사용자입니다."));
        if (user.getDeletedAt() != null) {
            throw new AlreadyLeftException("이미 탈퇴한 사용자입니다.");
        }

        user.unRegister();
        userRepository.save(user);
    }

    @Transactional
    public AuthToken refreshToken(String refreshToken) {
        jwtUtil.isValidTokenOrThrowException(refreshToken);

        Long userSeq = jwtUtil.extractUserSeq(refreshToken);
        String deviceId = jwtUtil.extractDeviceId(refreshToken);

        User user = userRepository.findById(userSeq).orElseThrow(() -> new UserNotFoundException("미가입 사용자입니다."));
        UserToken userToken = userTokenRepository.findByUserSeq(userSeq).orElseThrow(()-> new UserNotFoundException("미가입 사용자입니다."));

        // 저장된 Device나 Token이 일치하지 않을 경우
        if (!deviceId.equals(user.getDeviceId())
                || !refreshToken.equals(userToken.getToken())) {
            throw new NewLoginDetectedException("새로운 로그인이 감지되었습니다.");
        }

        // jwt 토큰 발급
        AuthToken authToken = jwtUtil.generateAuthToken(user.getSeq(), user.getDeviceId());

        // 토큰 저장
        saveJwtToken(user.getSeq(), authToken.getRefreshToken());

        return authToken;
    }

    public String getToken(String deviceId) {
        return jwtUtil.generateAccessToken(null, deviceId);
    }
}
