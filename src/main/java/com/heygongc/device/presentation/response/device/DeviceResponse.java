package com.heygongc.device.presentation.response.device;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "기기 정보 응답")
public record DeviceResponse (@Schema(description = "디바이스 ID") String deviceId,
                              @Schema(description = "디바이스 이름") String deviceName,
                              @Schema(description = "디바이스 배터리 잔량") int battery,
                              @Schema(description = "디바이스 온도") int temperature){
}

