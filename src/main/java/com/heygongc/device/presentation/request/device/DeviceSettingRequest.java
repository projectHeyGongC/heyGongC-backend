package com.heygongc.device.presentation.request.device;

import com.heygongc.device.domain.type.CameraOrientationType;
import com.heygongc.device.domain.type.SensitivityType;
import com.heygongc.global.common.request.RequestValidator;
import com.heygongc.global.utils.EnumUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.util.ObjectUtils;

@Schema(description = "기기 설정 변경 요청")
public record DeviceSettingRequest(
        @Schema(description = "소리 감지 민감도 정도. 민감도가 높을수록 더 작은 소리에도 기기가 반응한다.<br>" +
                "(VERYHIGH:매우 민감,<br>" +
                "HIGH:민감,<br>" +
                "MEDIUM: 보통,<br>" +
                "LOW: 둔감<br>" +
                "VERYLOW: 매우 둔감)", allowableValues = {"VERYHIGH","HIGH", "MEDIUM", "LOW", "VERYLOW"}) String sensitivity,
        @Schema(description = "카메라 방향(FRONT:전면, BACK:후면)", allowableValues = {"FRONT","BACK"}) String cameraOrientation
) implements RequestValidator {

    @Override
    public void validate() {
        if (ObjectUtils.isEmpty(this.sensitivity)) {
            throw new IllegalArgumentException("민감도는 필수입니다.");
        }

        if (EnumUtils.hasNoEnumConstant(SensitivityType.class, this.sensitivity)) {
            throw new IllegalArgumentException("민감도는 VERYHIGH, HIGH, MEDIUM, LOW, VERYLOW 중 하나여야합니다.");
        }

        if (ObjectUtils.isEmpty(this.cameraOrientation)) {
            throw new IllegalArgumentException("카메라 방향은 필수입니다.");
        }

        if (EnumUtils.hasNoEnumConstant(CameraOrientationType.class, this.cameraOrientation)) {
            throw new IllegalArgumentException("카메라 방향은 FRONT 또는 BACK이어야 합니다.");
        }
    }
}
