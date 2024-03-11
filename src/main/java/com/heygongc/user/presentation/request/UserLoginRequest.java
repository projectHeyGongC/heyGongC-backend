package com.heygongc.user.presentation.request;

import com.heygongc.global.utils.EnumUtils;
import com.heygongc.user.domain.type.OsType;
import com.heygongc.user.domain.type.SnsType;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.util.ObjectUtils;

@Schema(description = "사용자 로그인 요청")
public record UserLoginRequest(
        @Schema(description = "디바이스 ID") String deviceId,
        @Schema(description = "디바이스 OS") String deviceOs,
        @Schema(description = "SNS 타입(GOOGLE:구글,APPLE:애플)", allowableValues = {"GOOGLE","APPLE"}) String snsType,
        @Schema(description = "SNS 토큰") String accessToken
) {

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
