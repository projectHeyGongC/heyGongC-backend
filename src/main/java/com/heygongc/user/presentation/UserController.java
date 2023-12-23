package com.heygongc.user.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.heygongc.user.application.Token;
import com.heygongc.user.application.UserService;
import com.heygongc.user.presentation.request.UserCreateRequest;
import com.heygongc.user.presentation.request.UserLoginRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @GetMapping("/login/getGoogleLoginUrl")
    public ResponseEntity<String> getGoogleLoginUrl() {
        String reUrl = userService.getGoogleLoginUrl();
        logger.debug("getGoogleLoginURL >> {}", reUrl);
        return ResponseEntity.ok().body(reUrl);
    }

    @GetMapping("/login/googleLoginCallback")
    public ResponseEntity<Token> googleLoginCallback(@RequestParam(value = "code") String authCode) throws IOException {
        Token token = userService.getGoogleToken(authCode);
        logger.debug("googleLoginCallback >> {}", token.toString());
        return ResponseEntity.ok().body(token);
    }

    @PostMapping("/isUserExists")
    public ResponseEntity<Boolean> isUserExists(@RequestBody UserLoginRequest request) throws JsonProcessingException {
        return ResponseEntity.ok().body(userService.isUserExists(request));
    }

    @PostMapping("/login")
    public ResponseEntity<Boolean> loginUser(@RequestBody UserLoginRequest request) throws JsonProcessingException {
        return ResponseEntity.ok().body(userService.loginUser(request));
    }

    @PostMapping("/create")
    public ResponseEntity<Boolean> createUser(@RequestBody UserCreateRequest request) throws JsonProcessingException {
        return ResponseEntity.ok().body(userService.createUser(request));
    }
}
