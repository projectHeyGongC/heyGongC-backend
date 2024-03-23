package com.heygongc.device.presentation.request.camera;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.util.ObjectUtils;

public record CameraStatusRequest (
    @Schema(description = "디바이스 배터리 잔량") int battery,
    @Schema(description = "디바이스 온도") int temperature) {

    public void validate() {
        if (ObjectUtils.isEmpty(this.battery)) {
            throw new IllegalArgumentException("디바이스 배터리 잔량은 필수입니다.");
        }
        if (ObjectUtils.isEmpty(this.temperature)) {
            throw new IllegalArgumentException("디바이스 온도는 필수입니다.");
        }

    }
}
