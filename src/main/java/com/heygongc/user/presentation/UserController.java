package com.heygongc.user.presentation;

import com.heygongc.user.application.UserService;
import com.heygongc.user.presentation.request.UserLoginRequest;
import com.heygongc.user.presentation.request.UserRegisterRequest;
import com.heygongc.user.presentation.response.TokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/user")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{snsType}/getLoginUrl")
    public ResponseEntity<String> getLoginUrl(@PathVariable(name="snsType") String snsType) {
        String loginUrl = null;
        switch (snsType) {
            case "google":
                loginUrl = userService.getGoogleLoginUrl();
                break;
            case "apple":
//                loginUrl = userService.getGoogleLoginUrl();
                break;
            default:
                break;
        }
        logger.debug("getLoginUrl >> {}", loginUrl);
        return ResponseEntity.ok().body(loginUrl);
    }

    @GetMapping("/{snsType}/loginCallback")
    public ResponseEntity<TokenResponse> loginCallback(@PathVariable(name="snsType") String snsType, @RequestParam(value = "code") String code) {
        TokenResponse token = null;
        switch (snsType) {
            case "google":
                token = userService.getGoogleToken(code);
                break;
            case "apple":
//                token = userService.getGoogleToken(code);
                break;
            default:
                break;
        }
        if (token != null) {
            logger.debug("loginCallback >> {}", token.toString());
        }
        return ResponseEntity.ok().body(token);
    }

    @PostMapping("/{snsType}/login")
    public ResponseEntity<TokenResponse> login(@PathVariable(name="snsType") String snsType, @RequestBody UserLoginRequest request) {
        TokenResponse tokenResponse = null;
        switch (snsType) {
            case "google":
                tokenResponse = userService.googleLogin(request);
                break;
            case "apple":
//                tokenResponse = userService.loginUser(request);
                break;
            default:
                break;
        }
        if (tokenResponse != null) {
            logger.debug("login >> {}", tokenResponse.toString());
        }
        return ResponseEntity.ok().body(tokenResponse);
    }

    @PostMapping("/{snsType}/register")
    public ResponseEntity<TokenResponse> register(@PathVariable(name="snsType") String snsType, @RequestBody UserRegisterRequest request) {
        TokenResponse tokenResponse = null;
        switch (snsType) {
            case "google":
                tokenResponse = userService.googleRegister(request);
                break;
            case "apple":
//                tokenResponse = userService.register(request);
                break;
            default:
                break;
        }
        if (tokenResponse != null) {
            logger.debug("register >> {}", tokenResponse.toString());
        }
        return ResponseEntity.ok().body(tokenResponse);
    }
}
