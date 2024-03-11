package com.heygongc.user.application;

import com.heygongc.common.DatabaseCleaner;
import com.heygongc.user.domain.entity.User;
import com.heygongc.user.domain.repository.UserRepository;
import com.heygongc.user.domain.type.SnsType;
import com.heygongc.user.exception.UserNotFoundException;
import com.heygongc.user.presentation.request.RegisterRequest;
import com.heygongc.user.presentation.request.UserLoginRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.heygongc.user.fixture.OauthUserFixture.testGoogleUser;

@SpringBootTest
@ActiveProfiles("test")
@SuppressWarnings("NonAsciiCharacters")
class UserServiceTest {

    @Autowired
    private DatabaseCleaner cleaner;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


    @AfterEach
    void tearDown() {
        cleaner.execute();
    }

    @Test
    @DisplayName("가입한 사용자가 로그인 시 토큰을 발급한다")
    public void login() {
        // given
        구글_테스트_계정_등록();

        // when
        AuthToken token = userService.login(testGoogleUser(), userLoginRequest());

        // then
        Assertions.assertThat(token).isNotNull();
        Assertions.assertThat(token.getAccessToken()).isNotNull();
        Assertions.assertThat(token.getRefreshToken()).isNotNull();
    }

    @Test
    @DisplayName("미가입한 사용자가 로그인 시 에러가 발생한다.")
    public void loginWithIsUnRegistered() {
        // then
        Assertions.assertThatThrownBy(() -> userService.login(testGoogleUser(), userLoginRequest()))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("미가입한 사용자가 회원가입 시 토큰을 리턴한다")
    public void register() {
        // given & when
        AuthToken token = userService.register(testGoogleUser(), userRegisterRequest());

        // then
        Assertions.assertThat(token).isNotNull();
        Assertions.assertThat(token.getAccessToken()).isNotNull();
        Assertions.assertThat(token.getRefreshToken()).isNotNull();
    }

    private UserLoginRequest userLoginRequest() {
        return new UserLoginRequest("1111", "AOS", "google", "token");
    }

    private RegisterRequest userRegisterRequest() {
        return new RegisterRequest("1111", "AOS", true, "google", "token");
    }

    private User 구글_테스트_계정_등록() {
        return userRepository.save(User.createUser()
                .deviceId("1111")
                .snsId("123456789")
                .snsType(SnsType.GOOGLE)
                .deviceOs("AOS")
                .email("test@test.com")
                .alarm(true)
                .ads(true)
                .build());
    }
}