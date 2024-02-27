package com.heygongc.user.application;

import com.heygongc.user.application.apple.AppleOAuthUserProvider;
import com.heygongc.user.application.google.GoogleOAuth;
import com.heygongc.user.domain.User;
import com.heygongc.user.domain.UserRepository;
import com.heygongc.user.domain.UserToken;
import com.heygongc.user.domain.UserTokenRepository;
import com.heygongc.user.exception.AlreadyLeftException;
import com.heygongc.user.exception.AlreadySignUpException;
import com.heygongc.user.exception.NewLoginDetectedException;
import com.heygongc.user.exception.UserNotFoundException;
import com.heygongc.user.presentation.request.TokenRequest;
import com.heygongc.user.presentation.request.UserLoginRequest;
import com.heygongc.user.presentation.request.UserRegisterRequest;
import com.heygongc.user.presentation.response.OAuthUserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserTokenRepository userTokenRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private GoogleOAuth googleOAuth;

    @Mock
    private AppleOAuthUserProvider appleOAuthUserProvider;

    @InjectMocks
    private UserService userService;

    private String[] snsTypeArr;
    private User user;
    private Long userSeq;
    private Long unRegisterUserSeq;
    private UserLoginRequest userLoginRequest;
    private UserRegisterRequest userRegisterRequest;
    private OAuthUserResponse oAuthUserResponse;
    private UserToken userToken;
    private UserToken newUserToken;
    private AuthToken authToken;

    @BeforeEach
    public void setUp() {
        snsTypeArr = new String[]{"google", "apple"};
        userSeq = 1L;
        unRegisterUserSeq = 2L;

        user = User.builder()
                .seq(userSeq)
                .deviceId("mockDeviceId")
                .id("mockId")
                .email("mockEmail")
                .snsType(UserSnsType.GOOGLE)
                .ads(true)
                .build();

        userLoginRequest = new UserLoginRequest(
                "mockDeviceId",
                "mockDeviceOs",
                new TokenRequest(
                        "mockAccessToken",
                        "mockRefreshToken"));

        userRegisterRequest = new UserRegisterRequest(
                "mockDeviceId",
                "mockDeviceOs",
                true,
                new TokenRequest(
                        "mockAccessToken",
                        "mockRefreshToken"));

        userToken = new UserToken(
                "mockRefreshToken",
                User.builder().seq(userSeq).build());

        newUserToken = new UserToken(
                "mockNewRefreshToken",
                User.builder().seq(userSeq).build());

        oAuthUserResponse = new OAuthUserResponse(
                "mockSub",
                "mockEmail"
        );

        authToken = new AuthToken(
                "mockAccessToken",
                "mockRefreshToken");
    }

    @Test
    @DisplayName("가입한 사용자가 로그인 시 토큰을 발급한다")
    public void login() {
        // given
        when(userRepository.findBySnsId(anyString())).thenReturn(Optional.ofNullable(user));
        when(googleOAuth.getUser(anyString())).thenReturn(oAuthUserResponse);
        when(appleOAuthUserProvider.getApplePlatformMember(anyString())).thenReturn(oAuthUserResponse);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userTokenRepository.save(any(UserToken.class))).thenReturn(userToken);
        when(jwtUtil.generateAuthToken(any(), any())).thenReturn(authToken);

        for (String snsType : snsTypeArr) {
            // when
            AuthToken result = userService.login(snsType, userLoginRequest);

            // then
            assertNotNull(result);
            assertEquals(result.getAccessToken(), authToken.getAccessToken());
            assertEquals(result.getRefreshToken(), authToken.getRefreshToken());
        }
    }

    @Test
    @DisplayName("미가입한 사용자가 로그인 시 null을 리턴한다")
    public void loginWithIsUnRegistered() {
        // given
        when(googleOAuth.getUser(anyString())).thenReturn(oAuthUserResponse);
        when(appleOAuthUserProvider.getApplePlatformMember(anyString())).thenReturn(oAuthUserResponse);

        for (String snsType : snsTypeArr) {
            // when
            AuthToken result = userService.login(snsType, userLoginRequest);

            // then
            assertNull(result);
        }
    }

    @Test
    @DisplayName("미가입한 사용자가 회원가입 시 토큰을 리턴한다")
    public void register() {
        // given
        when(googleOAuth.getUser(anyString())).thenReturn(oAuthUserResponse);
        when(appleOAuthUserProvider.getApplePlatformMember(anyString())).thenReturn(oAuthUserResponse);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userTokenRepository.save(any(UserToken.class))).thenReturn(userToken);
        when(jwtUtil.generateAuthToken(any(), any())).thenReturn(authToken);

        for (String snsType : snsTypeArr) {
            // when
            AuthToken result = userService.register(snsType, userRegisterRequest);

            // then
            assertNotNull(result);
            assertEquals(result.getAccessToken(), authToken.getAccessToken());
            assertEquals(result.getRefreshToken(), authToken.getRefreshToken());
        }
    }

    @Test
    @DisplayName("가입한 사용자가 회원가입 시 예외를 리턴한다")
    public void registerWithIsRegistered() {
        // given
        when(userRepository.findBySnsId(anyString())).thenReturn(Optional.ofNullable(user));
        when(googleOAuth.getUser(anyString())).thenReturn(oAuthUserResponse);
        when(appleOAuthUserProvider.getApplePlatformMember(anyString())).thenReturn(oAuthUserResponse);

        for (String snsType : snsTypeArr) {
            // when
            assertThrows(AlreadySignUpException.class, () -> userService.register(snsType, userRegisterRequest));
        }
    }

    @Test
    @DisplayName("회원탈퇴 시 deletedAt 값을 저장한다")
    public void unRegister() {
        // given
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        userService.unRegister(userSeq);

        // then
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("미가입한 사용자가 회원탈퇴 시 예외를 리턴한다")
    public void unRegisterWithIsUnRegistered() {
        // given
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        // when
        assertThrows(UserNotFoundException.class, () -> userService.unRegister(unRegisterUserSeq));
    }

    @Test
    @DisplayName("이미 탈퇴한 사용자가 회원탈퇴 시 예외를 리턴한다")
    public void unRegisterWithAlreadyUnRegistered() {
        // given
        user.setDeletedAt(LocalDateTime.now());
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(user));

        // when
        assertThrows(AlreadyLeftException.class, () -> userService.unRegister(userSeq));
    }

    @Test
    @DisplayName("토큰 재발급 요청 시 액세스토큰과 리프레시토큰을 재발급한다")
    public void refreshToken() {
        // given
        when(jwtUtil.isValidToken(any())).thenReturn(true);
        when(jwtUtil.extractUserSeq(any())).thenReturn(user.getSeq());
        when(jwtUtil.extractDeviceId(any())).thenReturn(user.getDeviceId());
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(user));
        when(userTokenRepository.findByUserSeq(any())).thenReturn(Optional.ofNullable(userToken));
        when(jwtUtil.generateAuthToken(any(), any())).thenReturn(authToken);

        // when
        AuthToken result = userService.refreshToken(userToken.getToken());

        // then
        assertNotNull(result);
        assertEquals(result.getAccessToken(), authToken.getAccessToken());
        assertEquals(result.getRefreshToken(), authToken.getRefreshToken());
    }

    @Test
    @DisplayName("토큰 재발급 요청 시 신규 사용자가 로그인한 경우 예외를 리턴한다")
    public void refreshTokenWithIsNewUser() {
        // given
        when(jwtUtil.isValidToken(any())).thenReturn(true);
        when(jwtUtil.extractUserSeq(any())).thenReturn(user.getSeq());
        when(jwtUtil.extractDeviceId(any())).thenReturn(user.getDeviceId());
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(user));
        when(userTokenRepository.findByUserSeq(any())).thenReturn(Optional.ofNullable(newUserToken));

        // when
        assertThrows(NewLoginDetectedException.class, () -> userService.refreshToken(userToken.getToken()));
    }
}