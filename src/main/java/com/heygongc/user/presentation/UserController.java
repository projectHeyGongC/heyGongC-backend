package com.heygongc.user.presentation;

import com.heygongc.global.error.ErrorResponse;
import com.heygongc.user.application.AuthToken;
import com.heygongc.user.application.OauthService;
import com.heygongc.user.application.UserService;
import com.heygongc.user.application.oauth.OauthUser;
import com.heygongc.user.domain.entity.User;
import com.heygongc.user.presentation.request.RefreshTokenRequest;
import com.heygongc.user.presentation.request.UserLoginRequest;
import com.heygongc.user.presentation.request.RegisterRequest;
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

    private final OauthService oauthService;

    public UserController(UserService userService, OauthService oauthService) {
        this.userService = userService;
        this.oauthService = oauthService;
    }

    @PostMapping("/login")
    @Operation(
            summary = "사용자 로그인",
            description = "구글/애플 액세스 토큰을 이용해 사용자를 조회하여 로그인합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK(로그인 성공)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TokenResponse.class))),
                    @ApiResponse(responseCode = "400", description = "USER_NOT_FOUND(회원가입 필요)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<TokenResponse> login(
            @Parameter(name = "UserLoginRequest", description = "로그인 요청 정보", required = true) @RequestBody UserLoginRequest request) {
        OauthUser oAuthUser = oauthService.getOAuthUser(request.snsType(), request.accessToken());
        AuthToken authToken = userService.login(oAuthUser, request);
        return ResponseEntity.ok()
                .body(
                        new TokenResponse(authToken.getAccessToken(), authToken.getRefreshToken())
                );
    }

    @PostMapping("/register")
    @Operation(
            summary = "사용자 회원가입",
            description = "구글/애플 액세스 토큰을 이용해 사용자를 조회하여 회원가입합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TokenResponse.class)))
            }
    )
    public ResponseEntity<TokenResponse> register(@Parameter(name = "RegisterRequest", description = "회원가입 요청 정보", required = true)
                                                  @RequestBody RegisterRequest request) {
        OauthUser oAuthUser = oauthService.getOAuthUser(request.snsType(), request.accessToken());
        AuthToken authToken = userService.register(oAuthUser, request);
        return ResponseEntity.ok()
                .body(
                        new TokenResponse(authToken.getAccessToken(), authToken.getRefreshToken())
                );
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
            @Parameter(hidden = true) User user) {
        userService.unRegister(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/token/refresh")
    @Operation(
            summary = "액세스 토큰 재발급",
            description = "갱신 토큰을 이용해 액세스 토큰과 리프레시 토큰을 재발급합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK(액세스 토큰 반환)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TokenResponse.class))),
                    @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<TokenResponse> refreshToken(
            @Parameter(name = "RefreshTokenRequest", description = "토큰 갱신 요청 정보", required = true, in = ParameterIn.HEADER) @RequestBody RefreshTokenRequest request) {
        AuthToken authToken = userService.refreshToken(request.refreshToken());
        TokenResponse tokenResponse = new TokenResponse(authToken.getAccessToken(), authToken.getRefreshToken());
        return ResponseEntity.ok().body(tokenResponse);
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
    public ResponseEntity<UserResponse> getUserInfo(@Parameter(hidden = true) User user) {
        UserResponse userResponse = new UserResponse(
                user.getSnsType().name(),
                user.getEmail(),
                user.getAlarm()
        );
        return ResponseEntity.ok().body(userResponse);
    }
}
