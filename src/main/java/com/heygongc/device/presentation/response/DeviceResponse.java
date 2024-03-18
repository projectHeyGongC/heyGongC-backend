package com.heygongc.device.presentation.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "기기 정보 응답")
public record DeviceResponse (


        @Schema(description = "기기 기종")
        String type,
        @Schema(description = "기기 이름")
        String name


){

}

