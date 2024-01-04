package com.heygongc.user.application;

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
import com.heygongc.user.presentation.response.GoogleUserResponse;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.when;

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

    @InjectMocks
    private UserService userService;

    private User user;
    UserToken userToken;
    GoogleUserResponse googleUserResponse;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .seq(1L)
                .deviceId("mockDeviceId")
                .id("mockId")
                .email("mockEmail")
                .snsType(UserSnsType.GOOGLE)
                .ads(true)
                .build();

        userToken = new UserToken(
                "mockJwtRefreshTokenResponse",
                User.builder().seq(1L).build());

        googleUserResponse = new GoogleUserResponse(
                "mockIss",
                "mockAzp",
                "mockAud",
                "mockSub",
                "mockEmail",
                "mockEmailVerified",
                "mockAtHash",
                "mockName",
                "mockPicture",
                "mockGivenName",
                "mockFamilyName",
                "mockLocale",
                "mockIat",
                "mockExp",
                "mockAlg",
                "mockKid",
                "mockTyp",
                "mockScope"
        );
    }

    @Test
    public void 로그인_성공_테스트() {
        // given
        UserLoginRequest userLoginRequest = new UserLoginRequest(
                "mockDeviceId",
                "mockDeviceOs",
                new TokenRequest(
                        "mockAccessTokenRequest",
                        "mockRefreshTokenRequest"));

        //가입된 회원
        when(userRepository.findBySnsId(anyString())).thenReturn(Optional.ofNullable(user));

        when(googleOAuth.getUser(anyString())).thenReturn(googleUserResponse);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userTokenRepository.save(any(UserToken.class))).thenReturn(userToken);

        when(jwtUtil.generateAccessToken(anyString(), anyString())).thenReturn("mockJwtAccessTokenResponse");
        when(jwtUtil.generateRefreshToken(anyString(), anyString())).thenReturn("mockJwtRefreshTokenResponse");

        // when
        AuthToken result = userService.login("google", userLoginRequest);

        // then
        assertNotNull(result);
        assertEquals("mockJwtAccessTokenResponse", result.getAccessToken());
        assertEquals("mockJwtRefreshTokenResponse", result.getRefreshToken());
    }

    @Test
    // 미가입 사용자 로그인 실패, null 리턴하여 회원가입 이동
    public void 로그인_미가입_실패_테스트() {
        // given
        UserLoginRequest userLoginRequest = new UserLoginRequest(
                "mockDeviceId",
                "mockDeviceOs",
                new TokenRequest(
                        "mockAccessTokenRequest",
                        "mockRefreshTokenRequest"));

        when(googleOAuth.getUser(anyString())).thenReturn(googleUserResponse);

        // when
        AuthToken result = userService.login("google", userLoginRequest);

        // then
        assertNull(result);
    }

    @Test
    public void 회원가입_성공_테스트() {
        // given
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest(
                "mockDeviceId",
                "mockDeviceOs",
                true,
                new TokenRequest(
                        "mockAccessTokenRequest",
                        "mockRefreshTokenRequest"));

        when(googleOAuth.getUser(anyString())).thenReturn(googleUserResponse);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userTokenRepository.save(any(UserToken.class))).thenReturn(userToken);

        when(jwtUtil.generateAccessToken(anyString(), anyString())).thenReturn("mockJwtAccessTokenResponse");
        when(jwtUtil.generateRefreshToken(anyString(), anyString())).thenReturn("mockJwtRefreshTokenResponse");

        // when
        AuthToken result = userService.register("google", userRegisterRequest);

        // then
        assertNotNull(result);
        assertEquals("mockJwtAccessTokenResponse", result.getAccessToken());
        assertEquals("mockJwtRefreshTokenResponse", result.getRefreshToken());
    }

    @Test
    // 이미 가입된 사용자가 회원가입 요청 시 실패 테스트
    public void 회원가입_기가입_실패_테스트() {
        // given
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest(
                "mockDeviceId",
                "mockDeviceOs",
                true,
                new TokenRequest(
                        "mockAccessTokenRequest",
                        "mockRefreshTokenRequest"));

        //가입된 회원
        when(userRepository.findBySnsId(anyString())).thenReturn(Optional.ofNullable(user));

        when(googleOAuth.getUser(anyString())).thenReturn(googleUserResponse);

        // when
        assertThrows(AlreadySignUpException.class, () -> userService.register("google", userRegisterRequest));
    }

    @Test
    public void 회원탈퇴_성공_테스트() {
        // given
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        Boolean result = userService.unRegister(1L);

        // then
        assertTrue(result);
    }

    @Test
    // 미가입 사용자 실패 테스트
    public void 회원탈퇴_미가입_실패_테스트() {
        // given
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        // when
        assertThrows(UserNotFoundException.class, () -> userService.unRegister(2L));
    }

    @Test
    // 이미 탈퇴한 사용자 실패 테스트
    public void 회원탈퇴_기탈퇴_실패_테스트() {
        // given
        user.setDeletedAt(LocalDateTime.now());
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(user));

        // when
        assertThrows(AlreadyLeftException.class, () -> userService.unRegister(1L));
    }

    @Test
    public void 토큰_재발급_성공_테스트() {
        // given
        when(jwtUtil.isValidToken(any())).thenReturn(true);
        when(jwtUtil.extractUserSeq(any())).thenReturn(user.getSeq());
        when(jwtUtil.extractDeviceId(any())).thenReturn(user.getDeviceId());
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(user));
        when(userTokenRepository.findByUserSeq(any())).thenReturn(Optional.ofNullable(userToken));
        when(jwtUtil.generateAccessToken(any(),any())).thenReturn("newAccessToken");

        // when
        String token = userService.refreshAccessToken(userToken.getToken());

        // then
        assertNotNull(token);
        assertEquals("newAccessToken", token);
    }

    @Test
    // 신규 사용자 로그인으로 인한 실패 테스트
    public void 토큰_재발급_신규로그인_실패_테스트() {
        // given
        when(jwtUtil.isValidToken(any())).thenReturn(true);
        when(jwtUtil.extractUserSeq(any())).thenReturn(user.getSeq());
        when(jwtUtil.extractDeviceId(any())).thenReturn(user.getDeviceId());
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(user));
        when(userTokenRepository.findByUserSeq(any())).thenReturn(Optional.ofNullable(userToken));

        // when
        assertThrows(NewLoginDetectedException.class, () -> userService.refreshAccessToken("newAccessToken"));
    }
}