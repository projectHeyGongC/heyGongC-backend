package com.heygongc.user.application;

import com.heygongc.common.ServiceTest;
import com.heygongc.user.domain.entity.User;
import com.heygongc.user.domain.repository.UserRepository;
import com.heygongc.user.exception.UserNotFoundException;
import com.heygongc.user.presentation.request.RegisterRequest;
import com.heygongc.user.presentation.request.UserLoginRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.heygongc.user.setup.OauthUserSetup.testGoogleUser;
import static com.heygongc.user.setup.UserSetup.saveGoogleUser;


@SuppressWarnings("NonAsciiCharacters")
class UserServiceTest extends ServiceTest {


    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


    @Test
    @DisplayName("가입한 사용자가 로그인 시 토큰을 발급한다")
    public void login() {
        // given
        saveGoogleUser();

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

    @Test
    public void 회원탈퇴() {
        // given
        User 구글테스트계정 = saveGoogleUser();

        // when
        userService.unRegister(구글테스트계정);
        User 삭제된계정 = userRepository.findById(구글테스트계정.getUserSeq()).get();

        // then
        Assertions.assertThat(삭제된계정.getUserId()).isEqualTo("*****");
        Assertions.assertThat(삭제된계정.getSnsId()).isEqualTo("*****");
        Assertions.assertThat(삭제된계정.getEmail()).isEqualTo("*****@*****.***");
        Assertions.assertThat(삭제된계정.getDeviceId()).isEqualTo("*****");
        Assertions.assertThat(삭제된계정.getFcmToken()).isNull();
        Assertions.assertThat(삭제된계정.getDeletedAt()).isNotNull();
    }

    private UserLoginRequest userLoginRequest() {
        return new UserLoginRequest("1111","AOS", "google", "token", "fcmToken");
    }

    private RegisterRequest userRegisterRequest() {
        return new RegisterRequest("1111", "AOS", true, "google", "token", "fcmToken");
    }
}