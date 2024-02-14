package com.heygongc.notification.presentation.request;

import com.heygongc.notification.application.NotificationTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "알림 정보")
public record NotificationInfoRequest(
        @Schema(description = "알림 타입")
        NotificationTypeEnum type,

        @Schema(description = "알림 내용")
        String content
) {
}
