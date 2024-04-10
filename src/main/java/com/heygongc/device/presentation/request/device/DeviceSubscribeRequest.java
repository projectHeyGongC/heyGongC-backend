package com.heygongc.device.presentation.request.device;

import com.heygongc.global.common.request.RequestValidator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.util.ObjectUtils;

@Schema(description = "기기 연동 요청")
public record DeviceSubscribeRequest(
        @Schema(description = "디바이스 ID") String deviceId,
        @Schema(description = "디바이스 이름") String deviceName
) implements RequestValidator {

        @Override
        public void validate() {
                if (ObjectUtils.isEmpty(this.deviceId)) {
                        throw new IllegalArgumentException("디바이스 ID는 필수입니다.");
                }

                if (ObjectUtils.isEmpty(this.deviceName)) {
                        throw new IllegalArgumentException("디바이스 이름은 필수입니다.");
                }
        }
}
