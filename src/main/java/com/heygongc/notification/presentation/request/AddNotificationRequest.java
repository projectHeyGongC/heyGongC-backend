package com.heygongc.notification.presentation.request;

import com.heygongc.global.common.request.RequestValidator;
import com.heygongc.global.utils.EnumUtils;
import com.heygongc.notification.domain.type.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.util.ObjectUtils;

@Schema(title = "알림 정보")
public record AddNotificationRequest(
        @Schema(description = "알림 타입(SOUND:소리감지알림,DEVICE:기기연동알림)", allowableValues = {"SOUND", "DEVICE"}) String type,
        @Schema(description = "디바이스 시퀀스") String deviceId
    ) implements RequestValidator {

    @Override
    public void validate() {
        if (ObjectUtils.isEmpty(this.type)) {
            throw new IllegalArgumentException("알림 타입은 필수입니다.");
        }
        if (EnumUtils.hasNoEnumConstant(NotificationType.class, this.type)) {
            throw new IllegalArgumentException("알림 타입은 SOUND 또는 DEVICE이어야 합니다.");
        }
        if (ObjectUtils.isEmpty(this.deviceId)) {
            throw new IllegalArgumentException("디바이스 시퀀스는 필수입니다.");
        }
    }
}
