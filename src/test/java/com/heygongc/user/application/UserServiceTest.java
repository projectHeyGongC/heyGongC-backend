package com.heygongc.user.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.heygongc.user.domain.User;
import com.heygongc.user.domain.UserRepository;
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
    public void 회원테스트() throws JsonProcessingException {
        // given
        User user = User.builder()
                        .deviceId("testDeviceId001")
                        .id("testId001")
                        .email("testEmail001")
                        .snsType(UserSnsType.GOOGLE)
                        .ads(true)
                        .refreshToken("testRefreshToken001")
                        .build();
        when(userRepository.save(any())).thenReturn(user);

        // when
        User result = null;
//        User result = userService.createUser(new UserCreateRequest(
//                "testDeviceId001",
//                true,
//                new Token("testAccessToken001", "testRefreshToken001")
//        ));

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("testEmail001");
    }
}