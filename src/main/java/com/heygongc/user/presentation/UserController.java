package com.heygongc.user.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.heygongc.user.application.UserService;
import com.heygongc.user.domain.User;
import com.heygongc.user.presentation.request.UserCreateRequest;
import com.heygongc.user.presentation.request.UserLoginRequest;
import com.heygongc.user.presentation.response.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/v1/user")
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

    @GetMapping("/login/getGoogleLoginUrl")
    public ResponseEntity<String> getGoogleLoginUrl() {
        String reUrl = "https://accounts.google.com/o/oauth2/v2/auth?client_id=" +
                GOOGLE_CLIENT_ID +
                "&redirect_uri=" +
                GOOGLE_LOGIN_REDIRECT_URI +
                "&response_type=code" +
                "&scope=email profile";
        logger.debug("getGoogleLoginURL >> {}", reUrl);
        return ResponseEntity.ok().body(reUrl);
    }

    @GetMapping("/login/googleLoginCallback")
    public ResponseEntity<UserResponse> googleLoginCallback(@RequestParam(value = "code") String authCode) throws IOException {
        User user = userService.getGoogleUserInfo(authCode);
        UserResponse userResponse = new UserResponse(user.getSeq(), user.getDevice_id(), user.getId(), user.getEmail(), user.getSns_type(), user.getAccess_token(), user.getRefresh_token());
        logger.debug("googleLoginCallback >> {}", userResponse.toString());
        return ResponseEntity.ok().body(userResponse);
    }

    @PostMapping("/isUserExists")
    public ResponseEntity<Boolean> isUserExists(@RequestBody UserLoginRequest request) throws JsonProcessingException {
        return ResponseEntity.ok().body(userService.isUserExists(request));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> loginUser(@RequestBody UserLoginRequest request) throws JsonProcessingException {
        User user = userService.loginUser(request);
        UserResponse userResponse = new UserResponse(user.getSeq(), user.getDevice_id(), user.getId(), user.getEmail(), user.getSns_type(), user.getAccess_token(), user.getRefresh_token());
        return ResponseEntity.ok().body(userResponse);
    }

    @PostMapping("/create")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserCreateRequest request) {
        User user = userService.createUser(request);
        UserResponse userResponse = new UserResponse(user.getSeq(), user.getDevice_id(), user.getId(), user.getEmail(), user.getSns_type(), user.getAccess_token(), user.getRefresh_token());
        return ResponseEntity.ok().body(userResponse);
    }
}
