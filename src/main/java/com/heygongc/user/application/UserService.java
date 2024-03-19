package com.heygongc.user.application;

import com.heygongc.global.type.OsType;
import com.heygongc.user.application.oauth.OauthFactory;
import com.heygongc.user.application.oauth.OauthUser;
import com.heygongc.user.domain.entity.User;
import com.heygongc.user.domain.repository.UserRepository;
import com.heygongc.user.domain.entity.UserToken;
import com.heygongc.user.domain.repository.UserTokenRepository;
import com.heygongc.user.exception.*;
import com.heygongc.user.presentation.request.UserLoginRequest;
import com.heygongc.user.presentation.request.RegisterRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserRepository userRepository;
    private final UserTokenRepository userTokenRepository;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, UserTokenRepository userTokenRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.userTokenRepository = userTokenRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public AuthToken login(OauthUser oauthUser, UserLoginRequest request) {
        User user = userRepository.findBySnsId(oauthUser.id())
                .orElseThrow(() -> new UserNotFoundException("미가입 사용자입니다."));

        // device 정보 저장
        user.changeDevice(request.deviceId(), OsType.valueOf(request.deviceOs()));

        // jwt 토큰 발급
        AuthToken authToken = jwtUtil.generateAuthToken(user.getSeq(), user.getDeviceId());

        // 토큰 저장
        saveRefreshToken(user.getSeq(), authToken.getRefreshToken());

        return authToken;
    }

    @Transactional
    public AuthToken register(OauthUser oauthUser, RegisterRequest request) {
        if (existsUserBySnsId(oauthUser.id())) {
            throw new AlreadySignInException();
        }

        User user = userRepository.save(
                User.createUser()
                        .snsType(oauthUser.snsType())
                        .snsId(oauthUser.id())
                        .email(oauthUser.email())
                        .deviceId(request.deviceId())
                        .deviceOs(OsType.valueOf(request.deviceOs()))
                        .alarm(true)
                        .ads(request.ads())
                        .build());


        // jwt 토큰 발급
        AuthToken authToken = jwtUtil.generateAuthToken(user.getSeq(), user.getDeviceId());

        // 토큰 저장
        saveRefreshToken(user.getSeq(), authToken.getRefreshToken());

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
        jwtUtil.checkedValidTokenOrThrowException(refreshToken);

        Long userSeq = Long.parseLong(jwtUtil.extractSubject(refreshToken));
        String deviceId = jwtUtil.extractAudience(refreshToken);

        User user = userRepository.findById(userSeq).orElseThrow(() -> new UserNotFoundException("미가입 사용자입니다."));
        UserToken userToken = userTokenRepository.findByUserSeq(userSeq).orElseThrow(() -> new UserNotFoundException("미가입 사용자입니다."));

        // 저장된 Device나 Token이 일치하지 않을 경우
        if (!deviceId.equals(user.getDeviceId())
                || !refreshToken.equals(userToken.getToken())) {
            throw new NewLoginDetectedException("새로운 로그인이 감지되었습니다.");
        }

        // jwt 토큰 발급
        AuthToken authToken = jwtUtil.generateAuthToken(user.getSeq(), user.getDeviceId());

        // 토큰 저장
        saveRefreshToken(user.getSeq(), authToken.getRefreshToken());

        return authToken;
    }

    private boolean existsUserBySnsId(String snsId) {
        return userRepository.existsBySnsId(snsId);
    }

    private void saveRefreshToken(Long userSeq, String refreshToken) {
        // 이미 등록된 jwt 토큰이 있으면 삭제
        // TODO: 해야하는 의미가 있을까..?
        if (userTokenRepository.findByUserSeq(userSeq).isPresent()) {
            userTokenRepository.deleteToken(userSeq);
        }

        // jwt 토큰 저장
        UserToken userToken = UserToken.saveToken(refreshToken, userSeq);
        userTokenRepository.save(userToken);
    }

    // TODO: 분리 필요가 있어보임
    public String getToken(String deviceId) {
        return jwtUtil.generateAccessToken(null, deviceId);
    }
}
