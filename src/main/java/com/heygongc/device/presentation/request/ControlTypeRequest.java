package com.heygongc.device.presentation.request;

import com.heygongc.device.domain.type.ControlType;
import com.heygongc.global.utils.EnumUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.util.ObjectUtils;

@Schema(description = "카메라 앱 제어 요청")
public record ControlTypeRequest (
    @Schema(description = "명령할 컨트롤 타입<br>" +
            "(SOUNDON:소리감지 모드 켜기,<br>" +
            "SOUNDOFF:소리 감지 모드 끄기,<br>" +
            "STREAMON: 스트리밍 모드 켜기,<br>" +
            "STREAMOFF: 스트리밍 모드 끄기)", allowableValues = {"SOUNDON","SOUNDOFF", "STREAMON", "STREAMOFF"}) String controlType
) {
    public void validate() {
        if (ObjectUtils.isEmpty(this.controlType)) {
            throw new IllegalArgumentException("컨트롤 타입은 필수입니다.");
        }

        if (EnumUtils.hasNoEnumConstant(ControlType.class, this.controlType)) {
            throw new IllegalArgumentException("명령 타입은 SOUNDON, SOUNDOFF, STREAMON, STREAMOFF 중 하나여야합니다.");
        }

    }
}
