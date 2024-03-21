package com.heygongc.device.presentation.request;

import com.heygongc.device.domain.type.CameraModeType;
import com.heygongc.device.domain.type.SensitivityType;
import com.heygongc.global.utils.EnumUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.util.ObjectUtils;

public record CameraDeviceSettingRequest(
        @Schema(description = "소리 감지 민감도 정도. 민감도가 높을수록 더 작은 소리에도 기기가 반응한다.<br>" +
                "(VERYHIGH:매우 민감,<br>" +
                "HIGH:민감,<br>" +
                "MEDIUM: 보통,<br>" +
                "LOW: 둔감<br>" +
                "VERYLOW: 매우 둔감)", allowableValues = {"VERYHIGH","HIGH", "MEDIUM", "LOW", "VERYLOW"}) String sensitivity,

        @Schema(description = "카메라 모드(FRONT:전면 카메라,BACK:후면 카메라)", allowableValues = {"FRONT","BACK"}) String cameraMode


        ) {

    public void validate() {
        if (ObjectUtils.isEmpty(this.sensitivity)) {
            throw new IllegalArgumentException("민감도는 필수입니다.");
        }

        if (EnumUtils.hasNoEnumConstant(SensitivityType.class, this.sensitivity)) {
            throw new IllegalArgumentException("민감도는 VERYHIGH, HIGH, MEDIUM, LOW, VERYLOW 중 하나여야합니다.");
        }

        if (ObjectUtils.isEmpty(this.cameraMode)) {
            throw new IllegalArgumentException("카메라 모드는 필수입니다.");
        }

        if (EnumUtils.hasNoEnumConstant(CameraModeType.class, this.cameraMode)) {
            throw new IllegalArgumentException("카메라 모드는 FRONT 또는 BACK이어야 합니다.");
        }

    }
}
