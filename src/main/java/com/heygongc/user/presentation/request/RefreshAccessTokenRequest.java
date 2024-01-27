package com.heygongc.user.presentation.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "토큰 갱신 요청")
public record RefreshAccessTokenRequest(
    @Schema(description = "갱신 토큰")
    String refreshToken
) {
}