package com.heygongc.user.application;

import com.heygongc.user.application.application.UserService;
import com.heygongc.user.application.domain.User;
import com.heygongc.user.application.domain.UserRepository;
import com.heygongc.user.application.presentation.request.UserCreateRequest;
import com.heygongc.user.application.presentation.request.UserSnsType;
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
        User user = new User("TEST001", UserSnsType.GOOGLE, "test001@gmail.com", true, true);
        when(userRepository.save(any())).thenReturn(user);

        // when
        User result = userService.createTestUser(new UserCreateRequest("TEST001", UserSnsType.GOOGLE, "test001@gmail.com", true, true));

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("TEST001");
    }
}