package com.heygongc.device.presentation.request.camera;

import com.heygongc.global.common.request.RequestValidator;
import com.heygongc.global.type.OsType;
import com.heygongc.global.utils.EnumUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.util.ObjectUtils;

@Schema(description = "카메라 등록 요청")
public record CameraSubscribeRequest(
        @Schema(description = "디바이스 ID") String deviceId,
        @Schema(description = "디바이스 모델명") String modelName,
        @Schema(description = "디바이스 OS(AOS:안드로이드,IOS:아이폰)", allowableValues = {"AOS", "IOS"}) String deviceOs,
        @Schema(description = "fcm 토큰") String fcmToken
) implements RequestValidator {

    @Override
    public void validate() {
        if (ObjectUtils.isEmpty(this.deviceId)) {
            throw new IllegalArgumentException("디바이스 ID는 필수입니다.");
        }

        if (ObjectUtils.isEmpty(this.modelName)) {
            throw new IllegalArgumentException("디바이스 모델명은 필수입니다.");
        }

        if (ObjectUtils.isEmpty(this.deviceOs)) {
            throw new IllegalArgumentException("디바이스 OS는 필수입니다.");
        }

        if (EnumUtils.hasNoEnumConstant(OsType.class, this.deviceOs)) {
            throw new IllegalArgumentException("디바이스 OS는 AOS 또는 IOS이어야 합니다.");
        }

        if (ObjectUtils.isEmpty(this.fcmToken)) {
            throw new IllegalArgumentException("fcm 토큰은 필수입니다.");
        }
    }
}
