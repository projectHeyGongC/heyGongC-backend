package com.heygongc.common;

import com.heygongc.global.infra.FirebaseCloudMessaging;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Objects;

@Hidden
@RestController
public class TestController {

    private final FirebaseCloudMessaging firebaseCloudMessaging;

    public TestController(FirebaseCloudMessaging firebaseCloudMessaging) {
        this.firebaseCloudMessaging = firebaseCloudMessaging;
    }

    @PostMapping("/test/push")
    public void pushTest(@RequestBody TestPushDto dto) {
        firebaseCloudMessaging.sendMessage(dto.token, dto.title, dto.data);
    }


    public record TestPushDto(String token,
                                 String title,
                                 String body,
                                 HashMap<String, String> data) {
    }
}
