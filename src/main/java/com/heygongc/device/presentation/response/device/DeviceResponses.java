package com.heygongc.device.presentation.response.device;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record DeviceResponses(
        @Schema(description = "디바이스 목록") List<DeviceResponse> devices
) {

}
