package com.heygongc.notification.presentation.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "알림 정보 응답")
public record NotificationResponse (
        @Schema(description = "알림 내용")
        String content,
        @Schema(description = "발생 일시", allowableValues = {"true", "false"})
        LocalDateTime issuedAt
){
}
