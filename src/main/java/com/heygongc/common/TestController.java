package com.heygongc.common;

import com.heygongc.global.infra.FirebaseCloudMessaging;
import com.heygongc.global.infra.FirebaseData;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
public class TestController {

    private final FirebaseCloudMessaging firebaseCloudMessaging;

    public TestController(FirebaseCloudMessaging firebaseCloudMessaging) {
        this.firebaseCloudMessaging = firebaseCloudMessaging;
    }

    @PostMapping("/test/push")
    public void pushTest(@RequestBody TestPushDto dto) throws Exception {
        firebaseCloudMessaging.sendMessage(
                FirebaseData.builder()
                        .token(dto.token)
                        .title(dto.title)
                        .body(dto.body)
                        .action(dto.action)
                        .content(dto.content)
                        .build());
    }


    public record TestPushDto(String token,
                                 String title,
                                 String body,
                                 String action,
                                 String content) {
    }
}
