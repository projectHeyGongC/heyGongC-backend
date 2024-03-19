package com.heygongc.user.presentation.request;

import com.heygongc.global.utils.EnumUtils;
import com.heygongc.global.type.OsType;
import com.heygongc.user.domain.type.SnsType;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.util.ObjectUtils;

@Schema(title = "사용자 회원가입 요청")
public record RegisterRequest(@Schema(description = "디바이스 ID") String deviceId,
                              @Schema(description = "디바이스 OS(AOS:안드로이드,IOS:아이폰)", allowableValues = {"AOS", "IOS"}) String deviceOs,
                              @Schema(description = "마케팅 정보 수신 여부", allowableValues = {"true", "false"}) Boolean ads,
                              @Schema(description = "SNS 타입(GOOGLE:구글,APPLE:애플)", allowableValues = {"GOOGLE","APPLE"}) String snsType,
                              @Schema(description = "SNS 토큰") String accessToken) {

    public void validate() {
        if (ObjectUtils.isEmpty(this.deviceId)) {
            throw new IllegalArgumentException("디바이스 ID는 필수입니다.");
        }

        if (ObjectUtils.isEmpty(this.deviceOs)) {
            throw new IllegalArgumentException("디바이스 OS는 필수입니다.");
        }

        if (EnumUtils.hasNoEnumConstant(OsType.class, this.deviceOs)) {
            throw new IllegalArgumentException("디바이스 OS는 AOS 또는 IOS이어야 합니다.");
        }

        if (this.ads == null) {
            throw new IllegalArgumentException("마케팅 정보 수신 여부는 필수입니다.");
        }

        if (ObjectUtils.isEmpty(this.snsType)) {
            throw new IllegalArgumentException("SNS 타입은 필수입니다.");
        }

        if (EnumUtils.hasNoEnumConstant(SnsType.class, this.snsType)) {
            throw new IllegalArgumentException("SNS 타입은 GOOGLE 또는 APPLE이어야 합니다.");
        }

        if (ObjectUtils.isEmpty(this.accessToken)) {
            throw new IllegalArgumentException("SNS 토큰은 필수입니다.");
        }
    }
}
