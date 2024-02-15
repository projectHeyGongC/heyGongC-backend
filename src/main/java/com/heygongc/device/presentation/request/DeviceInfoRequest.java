package com.heygongc.device.presentation.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

@Schema(description = "기기 정보")
public record DeviceInfoRequest(
        @Schema(description = "디바이스 qr코드")
        @NotEmpty
        String deviceQR,
        @Schema(description = "디바이스 이름")
        @NotEmpty(message = "디바이스 이름은 필수입니다.")
        String name
){
        public Long getParsedDeviceSeq() {
                String[] parts = deviceQR.split("_");
                return Long.parseLong(parts[0]);
        }

        public String getType() {
                String[] parts = deviceQR.split("_");
                return parts[1];
        }
}
