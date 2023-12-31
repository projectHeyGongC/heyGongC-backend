package com.heygongc.user.application;

import com.heygongc.user.domain.User;
import com.heygongc.user.domain.UserRepository;
import com.heygongc.user.domain.UserToken;
import com.heygongc.user.domain.UserTokenRepository;
import com.heygongc.user.exception.EmailSigninException;
import com.heygongc.user.exception.UserNotFoundException;
import com.heygongc.user.presentation.request.UserLoginRequest;
import com.heygongc.user.presentation.request.UserRegisterRequest;
import com.heygongc.user.presentation.response.GoogleTokenResponse;
import com.heygongc.user.presentation.response.GoogleUserResponse;
import com.heygongc.user.presentation.response.TokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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

    public Boolean isUserExists(User user) {
        return user != null && user.getSeq() != null;
    }

    public Boolean isUserUnRegistered(User user) {
        return user != null && user.getDeletedAt() != null;
    }

    public String getLoginUrl(String snsType) {
        return switch (snsType) {
            case "google" -> googleOAuth.getLoginUrl();
//            case "apple" -> appleOAuth.getLoginUrl();
            default -> null;
        };
    }

    public TokenResponse getToken(String snsType, String code) {
        GoogleTokenResponse token = null;
        switch (snsType) {
            case "google":
                token = googleOAuth.getToken(code);
                break;
            case "apple":
//                token = appleOAuth.getToken(code);
                break;
            default:
                break;
        }
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

    public TokenResponse makeJwtToken(Long seq, String deviceId) {
        return new TokenResponse(
                jwtUtil.generateAccessToken(String.valueOf(seq), deviceId),
                jwtUtil.generateRefreshToken(String.valueOf(seq), deviceId)
        );
    }

    private UserToken saveJwtToken(Long seq, String refreshToken) {
        // 이미 등록된 jwt 토큰이 있으면 삭제
        if (userTokenRepository.findByUserSeq(seq).isPresent()) {
            userTokenRepository.deleteByUserSeq(seq);
        }

        // jwt 토큰 저장
        UserToken userToken = new UserToken(refreshToken, User.builder().seq(seq).build());
        userTokenRepository.save(userToken);

        return userToken;
    }

    @Transactional
    public TokenResponse login(String snsType, UserLoginRequest request) {
        User user = null;
        switch (snsType) {
            case "google":
                user = getGoogleUserInfo(request.token().accessToken());
                break;
            case "apple":
//                user = getAppleUserInfo(request.token().accessToken());
                break;
            default:
                break;
        }

        // 신규 가입인 경우 or 이미 탈퇴한 경우(재가입)
        if (!isUserExists(user) || isUserUnRegistered(user)) {
            return null;
        }
        
        // device 정보 저장
        user.setDeviceId(request.deviceId());
        user.setDeviceOs(request.deviceOs());
        userRepository.save(user);

        // jwt 토큰 발급
        TokenResponse tokenResponse = makeJwtToken(user.getSeq(), request.deviceId());

        // 토큰 저장
        saveJwtToken(user.getSeq(), tokenResponse.refreshToken());

        return tokenResponse;
    }

    @Transactional
    public TokenResponse register(String snsType, UserRegisterRequest request) {
        User user = null;
        switch (snsType) {
            case "google":
                user = getGoogleUserInfo(request.token().accessToken());
                break;
            case "apple":
//                user = getAppleUserInfo(request.token().accessToken());
                break;
            default:
                break;
        }
        // 탈퇴하지 않고 가입되어 있는 경우
        if (!isUserUnRegistered(user) && isUserExists(user)) {
            throw new EmailSigninException("이미 가입한 사용자입니다.");
        }

        User saveUser;
        if (isUserUnRegistered(user)) {
            // 탈퇴 후 재가입
            user.setDeviceId(request.deviceId());
            user.setDeviceOs(request.deviceOs());
            user.setAds(request.ads());
            user.setDeletedAt(null);
            saveUser = userRepository.save(user);
        } else {
            // 최초 회원가입
            saveUser = userRepository.save(
                    User.builder()
                            .deviceId(request.deviceId())
                            .deviceOs(request.deviceOs())
                            .id("USER" + ((int) (Math.random() * 9999) + 1)) // USER + 랜덤4자리
                            .snsType(UserSnsType.GOOGLE)
                            .snsId(user.getSnsId())
                            .email(user.getEmail())
                            .alarm(true)
                            .ads(request.ads())
                            .build()
            );
        }

        // jwt 토큰 발급
        TokenResponse tokenResponse = makeJwtToken(saveUser.getSeq(), request.deviceId());

        // 토큰 저장
        saveJwtToken(saveUser.getSeq(), tokenResponse.refreshToken());

        return tokenResponse;
    }

    @Transactional
    public Boolean unRegister(Long userSeq) {
        User user = userRepository.findById(userSeq).orElseThrow(() -> new UserNotFoundException("미가입 사용자입니다."));
        if (user.getDeletedAt() != null) {
            throw new UserNotFoundException("이미 탈퇴한 사용자입니다.");
        }

        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);

        return true;
    }
}
