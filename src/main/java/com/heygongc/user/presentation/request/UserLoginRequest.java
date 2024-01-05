package com.heygongc.user.presentation.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 로그인 요청")
public record UserLoginRequest(
        @Schema(description = "디바이스 ID")
        String deviceId,
        @Schema(description = "디바이스 OS")
        String deviceOs,
        @Schema(description = "SNS 토큰")
        TokenRequest token
) {
}
