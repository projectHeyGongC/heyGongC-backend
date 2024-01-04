package com.heygongc.user.presentation;

import com.heygongc.global.argumentresolver.LoginUser;
import com.heygongc.global.interceptor.Auth;
import com.heygongc.user.application.AuthToken;
import com.heygongc.user.application.UserService;
import com.heygongc.user.domain.User;
import com.heygongc.user.presentation.request.RefreshAccessTokenRequest;
import com.heygongc.user.presentation.request.UserLoginRequest;
import com.heygongc.user.presentation.request.UserRegisterRequest;
import com.heygongc.user.presentation.response.TokenResponse;
import jakarta.servlet.http.HttpServletRequest;
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
        String loginUrl = userService.getLoginUrl(snsType);
        return ResponseEntity.ok().body(loginUrl);
    }

    @GetMapping("/{snsType}/loginCallback")
    public ResponseEntity<TokenResponse> loginCallback(@PathVariable(name="snsType") String snsType, @RequestParam(value = "code") String code) {
        AuthToken authToken = userService.getToken(snsType, code);
        TokenResponse tokenResponse = new TokenResponse(authToken.getAccessToken(), authToken.getRefreshToken());
        return ResponseEntity.ok().body(tokenResponse);
    }

    @PostMapping("/{snsType}/login")
    public ResponseEntity<TokenResponse> login(@PathVariable(name="snsType") String snsType, @RequestBody UserLoginRequest request) {
        AuthToken authToken = userService.login(snsType, request);
        TokenResponse tokenResponse = new TokenResponse(authToken.getAccessToken(), authToken.getRefreshToken());
        return ResponseEntity.ok().body(tokenResponse);
    }

    @PostMapping("/{snsType}/register")
    public ResponseEntity<TokenResponse> register(@PathVariable(name="snsType") String snsType, @RequestBody UserRegisterRequest request) {
        AuthToken authToken = userService.register(snsType, request);
        TokenResponse tokenResponse = new TokenResponse(authToken.getAccessToken(), authToken.getRefreshToken());
        return ResponseEntity.ok().body(tokenResponse);
    }

    @Auth
    @PostMapping("/unRegister")
    public ResponseEntity<Boolean> unRegister(@LoginUser User user) {
        Long userSeq = user.getSeq();
        Boolean result = userService.unRegister(userSeq);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/refreshAccessToken")
    public ResponseEntity<String> refreshAccessToken(@RequestBody RefreshAccessTokenRequest request) {
        String accessToken = userService.refreshAccessToken(request.refreshToken());
        return ResponseEntity.ok().body(accessToken);
    }
}
