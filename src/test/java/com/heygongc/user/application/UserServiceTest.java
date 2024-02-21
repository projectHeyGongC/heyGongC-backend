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

    @InjectMocks
    private UserService userService;

    private String[] snsTypeArr;
    private User user;
    private Long userSeq;
    private Long unRegisterUserSeq;
    private UserLoginRequest userLoginRequest;
    private UserRegisterRequest userRegisterRequest;
    private GoogleUserResponse googleUserResponse;
    private UserToken userToken;
    private UserToken newUserToken;
    private AuthToken authToken;

    @BeforeEach
    public void setUp() {
        snsTypeArr = new String[]{"google"};
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

        authToken = new AuthToken(
                "mockAccessToken",
                "mockRefreshToken");
    }

    @Test
    public void 로그인_성공_테스트() {
        // given
        when(userRepository.findBySnsId(anyString())).thenReturn(Optional.ofNullable(user));
        when(googleOAuth.getUser(anyString())).thenReturn(googleUserResponse);
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
    // 미가입 사용자 로그인 실패, null 리턴하여 회원가입 이동
    public void 로그인_미가입_실패_테스트() {
        // given
        when(googleOAuth.getUser(anyString())).thenReturn(googleUserResponse);

        for (String snsType : snsTypeArr) {
            // when
            AuthToken result = userService.login(snsType, userLoginRequest);

            // then
            assertNull(result);
        }
    }

    @Test
    public void 회원가입_성공_테스트() {
        // given
        when(googleOAuth.getUser(anyString())).thenReturn(googleUserResponse);
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
    // 이미 가입된 사용자가 회원가입 요청 시 실패 테스트
    public void 회원가입_기가입_실패_테스트() {
        // given
        when(userRepository.findBySnsId(anyString())).thenReturn(Optional.ofNullable(user));
        when(googleOAuth.getUser(anyString())).thenReturn(googleUserResponse);

        for (String snsType : snsTypeArr) {
            // when
            assertThrows(AlreadySignUpException.class, () -> userService.register(snsType, userRegisterRequest));
        }
    }

    @Test
    public void 회원탈퇴_성공_테스트() {
        // given
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        userService.unRegister(userSeq);

        // then
        verify(userRepository, times(1)).save(user);
    }

    @Test
    // 미가입 사용자 실패 테스트
    public void 회원탈퇴_미가입_실패_테스트() {
        // given
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        // when
        assertThrows(UserNotFoundException.class, () -> userService.unRegister(unRegisterUserSeq));
    }

    @Test
    // 이미 탈퇴한 사용자 실패 테스트
    public void 회원탈퇴_기탈퇴_실패_테스트() {
        // given
        user.setDeletedAt(LocalDateTime.now());
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(user));

        // when
        assertThrows(AlreadyLeftException.class, () -> userService.unRegister(userSeq));
    }

    @Test
    public void 토큰_재발급_성공_테스트() {
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
    // 신규 사용자 로그인으로 인한 실패 테스트
    public void 토큰_재발급_신규로그인_실패_테스트() {
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