package com.heygongc.user.presentation;

import com.heygongc.global.argumentresolver.LoginUser;
import com.heygongc.global.error.ErrorResponse;
import com.heygongc.user.application.AuthToken;
import com.heygongc.user.application.UserService;
import com.heygongc.user.domain.User;
import com.heygongc.user.presentation.request.RefreshTokenRequest;
import com.heygongc.user.presentation.request.UserLoginRequest;
import com.heygongc.user.presentation.request.UserRegisterRequest;
import com.heygongc.user.presentation.response.TokenResponse;
import com.heygongc.user.presentation.response.UserResponse;
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
@RequestMapping("/v1/users")
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
                    @ApiResponse(responseCode = "204", description = "OK(회원가입 필요)", content = @Content)
            }
    )
    public ResponseEntity<TokenResponse> login(
            @Parameter(description = "SNS타입(google/apple)", required = true, in = ParameterIn.PATH) @PathVariable(name="snsType") String snsType,
            @Parameter(name = "UserLoginRequest", description = "로그인 요청 정보", required = true) @RequestBody UserLoginRequest request) {
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
            @Parameter(name = "UserRegisterRequest", description = "회원가입 요청 정보", required = true) @RequestBody UserRegisterRequest request) {
        AuthToken authToken = userService.register(snsType, request);
        TokenResponse tokenResponse = new TokenResponse(authToken.getAccessToken(), authToken.getRefreshToken());
        return ResponseEntity.ok().body(tokenResponse);
    }

    @PostMapping("/unregister")
    @Operation(
            summary = "사용자 탈퇴",
            description = "액세스 토큰을 이용해 회원탈퇴 처리합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content),
                    @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "새로운 로그인이 존재하는 경우", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<Void> unregister(
            @Parameter(hidden = true) @LoginUser User user) {
        Long userSeq = user.getSeq();
        userService.unRegister(userSeq);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/token/refresh")
    @Operation(
            summary = "액세스 토큰 재발급",
            description = "갱신 토큰을 이용해 액세스 토큰과 리프레시 토큰을 재발급합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK(액세스 토큰 반환)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "새로운 로그인이 존재하는 경우", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<TokenResponse> refreshToken(
            @Parameter(name = "RefreshTokenRequest", description = "토큰 갱신 요청 정보", required = true, in = ParameterIn.HEADER) @RequestBody RefreshTokenRequest request) {
        AuthToken authToken = userService.refreshToken(request.refreshToken());
        TokenResponse tokenResponse = new TokenResponse(authToken.getAccessToken(), authToken.getRefreshToken());
        return ResponseEntity.ok().body(tokenResponse);
    }

    @GetMapping("/token")
    @Operation(
            summary = "액세스 토큰 발급",
            description = "카메라 앱에서 사용할 액세스 토큰을 신규 발급합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class)))
            }
    )
    public ResponseEntity<String> getToken(
            @Parameter(name = "deviceId", description = "디바이스 ID", required = true, in = ParameterIn.QUERY) @RequestParam(name="deviceId") String deviceId) {
        String accessToken = userService.getToken(deviceId);
        return ResponseEntity.ok().body(accessToken);
    }

    @GetMapping("/info")
    @Operation(
            summary = "사용자 정보 조회",
            description = "액세스 토큰을 이용해 사용자 정보를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
                    @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "새로운 로그인이 존재하는 경우", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<UserResponse> getUserInfo(
        @Parameter(hidden = true) @LoginUser User user) {
        UserResponse userResponse = new UserResponse(
                user.getDeviceId(),
                user.getDeviceOs(),
                user.getSnsType().name(),
                user.getEmail(),
                user.getAlarm(),
                user.getAds()
        );
        return ResponseEntity.ok().body(userResponse);
    }
}
