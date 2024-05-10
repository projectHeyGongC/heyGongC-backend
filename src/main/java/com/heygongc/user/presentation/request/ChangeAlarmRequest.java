package com.heygongc.user.presentation.request;

import com.heygongc.global.common.request.RequestValidator;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "이벤트 알림 수신여부 변경 요청")
public record ChangeAlarmRequest (
    @Schema(description = "이벤트 알림 수신여부", allowableValues = {"true", "false"})
    Boolean alarm
) implements RequestValidator {
    @Override
    public void validate() {
        if (this.alarm == null) {
            throw new IllegalArgumentException("이벤트 알림 수신여부는 필수입니다.");
        }
    }
}
