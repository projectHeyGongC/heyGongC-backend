package com.heygongc.user.presentation.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "토큰 발급 요청")
public record AccessTokenRequest(
    @Schema(description = "디바이스 ID")
    String deviceId
) {
}