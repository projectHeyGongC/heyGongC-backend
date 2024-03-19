package com.heygongc.user.presentation.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 정보 응답")
public record UserResponse(
        @Schema(description = "디바이스 ID")
        String deviceId,
        @Schema(description = "디바이스 OS")
        String deviceOs,
        @Schema(description = "가입한 SNS 종류", allowableValues = {"GOOGLE", "APPLE"})
        String snsType,
        @Schema(description = "이메일")
        String email,
        @Schema(description = "알람 수신 여부", allowableValues = {"true", "false"})
        Boolean alarm,
        @Schema(description = "마케팅 정보 수신 여부", allowableValues = {"true", "false"})
        Boolean ads
) {
}