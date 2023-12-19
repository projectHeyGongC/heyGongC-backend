package com.heygongc.user.application.presentation;

import com.heygongc.user.application.application.UserService;
import com.heygongc.user.application.domain.User;
import com.heygongc.user.application.presentation.request.UserCreateRequest;
import com.heygongc.user.application.presentation.response.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/v1/users")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Value("${google.client.id}")
    private String GOOGLE_CLIENT_ID;
    @Value("${google.client.secret}")
    private String GOOGLE_CLIENT_SECRET;
    @Value("${google.client.redirect-uri}")
    private String GOOGLE_LOGIN_REDIRECT_URI;

    @GetMapping("/login/google")
    public ResponseEntity<String> getGoogleLoginURL() {
        String reUrl = "https://accounts.google.com/o/oauth2/v2/auth?client_id=" +
                GOOGLE_CLIENT_ID +
                "&redirect_uri=" +
                GOOGLE_LOGIN_REDIRECT_URI +
                "&response_type=code" +
                "&scope=email profile";
        logger.debug("getGoogleLoginURL >> " + reUrl);
        return ResponseEntity.ok().body(reUrl);
    }

    @GetMapping("/login/google/Callback")
    public ResponseEntity<User> googleLoginCallback(@RequestParam(value = "code") String authCode) throws IOException {
        return ResponseEntity.ok().body(userService.googleLogin(authCode));
    }

    @PostMapping("/create")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserCreateRequest request) {
        User user = userService.createTestUser(request);
        UserResponse userResponse = new UserResponse(user.getSeq(), user.getId(), user.getEmail());
        return ResponseEntity.ok().body(userResponse);
    }
}
