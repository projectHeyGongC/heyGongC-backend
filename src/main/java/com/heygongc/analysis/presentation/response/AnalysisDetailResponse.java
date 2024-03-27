package com.heygongc.analysis.presentation.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "분석 상세 응답")
public record AnalysisDetailResponse(
        @Schema(description = "디바이스 ID") String deviceId,
        @Schema(description = "디바이스 이름") String deviceName,
        @Schema(description = "조회일자") String requestAt,
        @Schema(description = "총 알람 개수") Long alarmCount,
        @Schema(description = "시간별 알림횟수") List<Graph> graph
) {
    public record Graph(
            @Schema(description = "알림 시간") String x,
            @Schema(description = "알림 횟수") Long y
    ) {}
}
