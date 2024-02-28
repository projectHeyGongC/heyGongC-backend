package com.heygongc.device.presentation.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

@Schema(description = "기기 정보")
public record DeviceInfoRequest(

        @Schema(description = "기기 아이디")
        String uuid,

        @Schema(description = "기기 기종")
        @NotEmpty
        String type,
        @Schema(description = "기기 이름")
        @NotEmpty(message = "디바이스 이름은 필수입니다.")
        String name
){

}
