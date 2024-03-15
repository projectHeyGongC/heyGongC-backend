package com.heygongc.user.presentation.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 정보 응답")
public record UserResponse(@Schema(description = "가입한 SNS 종류") String snsType,
                           @Schema(description = "이메일") String email,
                           @Schema(description = "알람 수신 여부") Boolean alarm) {
}