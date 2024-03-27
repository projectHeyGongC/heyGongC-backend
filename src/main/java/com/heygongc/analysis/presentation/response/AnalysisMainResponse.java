package com.heygongc.analysis.presentation.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "분석 정보 응답")
public record AnalysisMainResponse(
        @Schema(description = "알림 목록") List<Notifications> notifications,
        @Schema(description = "비디오 URL") String videos
) {
    public record Notifications(
            @Schema(description = "디바이스 ID") String deviceId,
            @Schema(description = "디바이스 이름") String deviceName,
            @Schema(description = "알림 내용") String contents
    ) {}
}
