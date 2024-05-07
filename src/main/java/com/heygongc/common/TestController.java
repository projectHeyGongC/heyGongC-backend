package com.heygongc.common;

import com.heygongc.auth.domain.AuthToken;
import com.heygongc.global.infra.FirebaseCloudMessaging;
import com.heygongc.global.infra.FirebaseData;
import com.heygongc.user.application.UserService;
import com.heygongc.user.presentation.response.TokenResponse;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequestMapping("/test")
public class TestController {

    private final FirebaseCloudMessaging firebaseCloudMessaging;
    private final UserService userService;

    public TestController(FirebaseCloudMessaging firebaseCloudMessaging, UserService userService) {
        this.firebaseCloudMessaging = firebaseCloudMessaging;
        this.userService = userService;
    }

    @PostMapping("/push")
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

    @PostMapping("/login")
    public ResponseEntity<?> testLogin(@RequestBody TestLoginDto dto) {
        AuthToken authToken = userService.testLogin(dto.userSeq, dto.snsId, dto.email);
        return ResponseEntity.ok()
                .body(
                        new TokenResponse(authToken.getAccessToken(), authToken.getRefreshToken())
                );
    }


    public record TestPushDto(String token,
                              String title,
                              String body,
                              String action,
                              String content) {}

    public record TestLoginDto(Long userSeq,
                               String email,
                               String snsId) {}
}
