package com.heygongc.notification.presentation.response;

import com.heygongc.notification.application.NotificationTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Schema(description = "알림 정보 응답")
public record NotificationResponse (

        @Schema(description = "알림 종류")
        NotificationTypeEnum typeEnum,

        @Schema(description = "알림 내용")
        String content,

        @Schema(description = "읽음 여부")
        Boolean readStatus

){

}
