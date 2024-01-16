package com.heygongc.user.presentation;

import com.heygongc.global.argumentresolver.LoginUser;
import com.heygongc.global.error.ErrorResponse;
import com.heygongc.user.application.AuthToken;
import com.heygongc.user.application.UserService;
import com.heygongc.user.domain.User;
import com.heygongc.user.presentation.request.RefreshAccessTokenRequest;
import com.heygongc.user.presentation.request.UserLoginRequest;
import com.heygongc.user.presentation.request.UserRegisterRequest;
import com.heygongc.user.presentation.response.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User API", description = "유저 API")
@RestController
@RequestMapping("/v1/user")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/{snsType}/login")
    @Operation(
            summary = "사용자 로그인",
            description = "구글/애플 액세스 토큰을 이용해 사용자를 조회하여 로그인합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK(로그인 성공)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TokenResponse.class))),
                    @ApiResponse(responseCode = "204", description = "OK(회원가입 필요)")
            }
    )
    public ResponseEntity<TokenResponse> login(
            @Parameter(description = "SNS타입(google/apple)", required = true, in = ParameterIn.PATH) @PathVariable(name="snsType") String snsType,
            @Parameter(name = "UserLoginRequest", description = "로그인 요청 정보", required = true, in = ParameterIn.HEADER) @RequestBody UserLoginRequest request) {
        AuthToken authToken = userService.login(snsType, request);
        // 회원가입 필요한 경우
        if (authToken == null) {
            return ResponseEntity.noContent().build();
        }
        TokenResponse tokenResponse = new TokenResponse(authToken.getAccessToken(), authToken.getRefreshToken());
        return ResponseEntity.ok().body(tokenResponse);
    }

    @PostMapping("/{snsType}/register")
    @Operation(
            summary = "사용자 회원가입",
            description = "구글/애플 액세스 토큰을 이용해 사용자를 조회하여 회원가입합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TokenResponse.class)))
            }
    )
    public ResponseEntity<TokenResponse> register(
            @Parameter(description = "SNS타입(google/apple)", required = true, in = ParameterIn.PATH) @PathVariable(name="snsType") String snsType,
            @Parameter(name = "UserRegisterRequest", description = "회원가입 요청 정보", required = true, in = ParameterIn.HEADER) @RequestBody UserRegisterRequest request) {
        AuthToken authToken = userService.register(snsType, request);
        TokenResponse tokenResponse = new TokenResponse(authToken.getAccessToken(), authToken.getRefreshToken());
        return ResponseEntity.ok().body(tokenResponse);
    }

    @PostMapping("/unRegister")
    @Operation(
            summary = "사용자 탈퇴",
            description = "액세스 토큰을 이용해 회원탈퇴 처리합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "새로운 로그인이 존재하는 경우", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<Void> unRegister(
            @Parameter(hidden = true) @LoginUser User user) {
        Long userSeq = user.getSeq();
        userService.unRegister(userSeq);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refreshAccessToken")
    @Operation(
            summary = "액세스 토큰 재발급",
            description = "갱신 토큰을 이용해 액세스 토큰을 재발급합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK(액세스 토큰 반환)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "새로운 로그인이 존재하는 경우", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<String> refreshAccessToken(
            @Parameter(name = "RefreshAccessTokenRequest", description = "토큰 갱신 요청 정보", required = true, in = ParameterIn.HEADER) @RequestBody RefreshAccessTokenRequest request) {
        String accessToken = userService.refreshAccessToken(request.refreshToken());
        return ResponseEntity.ok().body(accessToken);
    }
}
