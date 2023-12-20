package com.heygongc.user.application;

import com.heygongc.user.domain.User;
import com.heygongc.user.domain.UserRepository;
import com.heygongc.user.presentation.request.UserCreateRequest;
import com.heygongc.user.presentation.request.UserSnsType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void 회원테스트() {
        // given
        User user = User.builder()
                        .device_id("testDeviceId001")
                        .id("testId001")
                        .email("testEmail001")
                        .sns_type(UserSnsType.GOOGLE)
                        .ads(true)
                        .access_token("testAccessToken001")
                        .refresh_token("testRefreshToken001")
                        .build();
        when(userRepository.save(any())).thenReturn(user);

        // when
        User result = userService.createUser(new UserCreateRequest(
                "testDeviceId001",
                "testEmail001",
                UserSnsType.GOOGLE,
                "testEmail001",
                true,
                "testAccessToken001",
                "testRefreshToken001"
        ));

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("testEmail001");
    }
}