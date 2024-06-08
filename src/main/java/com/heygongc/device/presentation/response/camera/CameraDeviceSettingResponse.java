package com.heygongc.device.presentation.response.camera;

import io.swagger.v3.oas.annotations.media.Schema;

public record CameraDeviceSettingResponse(
        @Schema(description = "소리감지모드 상태(ON, OFF)") String soundSensingStatus,
        @Schema(description = "소리 감지 민감도 정도. 민감도가 높을수록 더 작은 소리에도 기기가 반응한다.<br>" +
                "(VERYHIGH:매우 민감,<br>" +
                "HIGH:민감,<br>" +
                "MEDIUM: 보통,<br>" +
                "LOW: 둔감<br>" +
                "VERYLOW: 매우 둔감)") String sensitivity,
        @Schema(description = "카메라 방향(FRONT:전면, BACK:후면)") String cameraOrientation
) {
}