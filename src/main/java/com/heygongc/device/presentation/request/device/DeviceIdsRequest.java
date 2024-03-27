package com.heygongc.device.presentation.request.device;

import com.heygongc.global.common.request.RequestValidator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Schema(description = "메인 앱에서 카메라 앱 기기 정보 요청")
public record DeviceIdsRequest(
        @Schema(description = "디바이스 ID 목록") List<String> deviceIds
) implements RequestValidator {

    @Override
    public void validate() {
        if (ObjectUtils.isEmpty(this.deviceIds)) {
            throw new IllegalArgumentException("하나 이상의 디바이스 ID가 필요합니다.");
        }
    }
}