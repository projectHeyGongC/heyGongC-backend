package com.heygongc.user.presentation.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 회원가입 요청")
public record UserRegisterRequest(
        @Schema(description = "디바이스 ID")
        String deviceId,
        @Schema(description = "디바이스 OS")
        String deviceOs,
        @Schema(description = "마케팅 정보 수신 여부", allowableValues = {"true", "false"})
        boolean ads,
        @Schema(description = "SNS 토큰")
        TokenRequest token
) {
}
