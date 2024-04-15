package com.heygongc.device.presentation.response.device;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "기기 상세 정보 조회 응답")
public record DeviceInfoResponse(@Schema(description = "디바이스 ID") String deviceId,
                                 @Schema(description = "디바이스 이름") String deviceName,
                                 @Schema(description = "모델명") String modelName,
                                 @Schema(description = "민감도") String sensitivity,
                                 @Schema(description = "카메라 모드") String cameraMode,
                                 @Schema(description = "소리감지모드 상태") String soundStatus) {
}
