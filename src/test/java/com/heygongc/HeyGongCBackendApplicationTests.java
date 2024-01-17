package com.heygongc;

import com.heygongc.user.presentation.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySource(locations = "classpath:/config/application.yml") // 테스트에 사용할 프로퍼티 파일 경로 지정
class HeyGongCBackendApplicationTests {

    @Autowired
    private UserController userController;

    @Test
    void contextLoads() {
        assertNotNull(userController);
    }

}
