package com.heygongc.device.presentation.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "기기 정보")
public record DeviceInfoRequest(
        @Schema(description = "디바이스 qr코드")
        String deviceQR,
        @Schema(description = "디바이스 이름")
        String name
){

}
