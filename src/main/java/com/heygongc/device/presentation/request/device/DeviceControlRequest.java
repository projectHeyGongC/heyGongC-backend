package com.heygongc.device.presentation.request.device;

import com.heygongc.device.domain.type.CameraOrientationType;
import com.heygongc.device.domain.type.SensitivityType;
import com.heygongc.global.common.request.RequestValidator;
import com.heygongc.global.type.FcmActionType;
import com.heygongc.global.utils.EnumUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.util.ObjectUtils;

@Schema(description = "기기 제어 요청")
public record DeviceControlRequest(
    @Schema(description = "명령할 컨트롤 타입", allowableValues = {
            "SENSITIVITY",
            "CAMERA_ORIENTATION",
            "SOUND_SENSING",
            "STREAM",
            "CAMERA",
            "FLASH",
            "SPEAKING",
            "LOW_LIGHT",
            "REMOTE_EXECUTION",
            "REMOTE_SHUTDOWN"
    }) String controlType,
    @Schema(description = "명령할 컨트롤 모드(ON/OFF)") String controlMode
) implements RequestValidator {

    @Override
    public void validate() {
        if (ObjectUtils.isEmpty(this.controlType)) {
            throw new IllegalArgumentException("컨트롤 타입은 필수입니다.");
        }

        if (EnumUtils.hasNoEnumConstant(FcmActionType.class, this.controlType)) {
            throw new IllegalArgumentException("명령 타입은 SENSITIVITY, CAMERA_ORIENTATION, SOUND_SENSING, STREAM, CAMERA, FLASH, SPEAKING, LOW_LIGHT, REMOTE_EXECUTION, REMOTE_SHUTDOWN 중 하나여야합니다.");
        }

        if (ObjectUtils.isEmpty(this.controlMode)) {
            throw new IllegalArgumentException("명령할 컨트롤 모드는 필수입니다.");
        }

        switch (this.controlType) {
            case "SENSITIVITY":
                if (EnumUtils.hasNoEnumConstant(SensitivityType.class, this.controlMode)) {
                    throw new IllegalArgumentException("민감도는 VERYHIGH, HIGH, MEDIUM, LOW, VERYLOW 중 하나여야 합니다.");
                }
                break;
            case "CAMERA_ORIENTATION":
                if (EnumUtils.hasNoEnumConstant(CameraOrientationType.class, this.controlMode)) {
                    throw new IllegalArgumentException("카메라 방향은 FRONT, BACK 중 하나여야 합니다.");
                }
                break;
        }
    }
}
