package com.heygongc.device.presentation.response.device;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "기기 목록 응답")
public record DeviceListResponse(@Schema(description = "디바이스 ID") String deviceId,
                                 @Schema(description = "디바이스 이름") String deviceName,
                                 @Schema(description = "디바이스 배터리 잔량") int battery,
                                 @Schema(description = "디바이스 온도") int temperature,
                                 @Schema(description = "디바이스 연결 상태") String connectStatus,
                                 @Schema(description = "소리감지모드 상태") String soundSensingStatus) {
}
