package com.heygongc.analysis.presentation;

import com.heygongc.analysis.application.AnalysisService;
import com.heygongc.analysis.presentation.response.AnalysisDetailResponse;
import com.heygongc.analysis.presentation.response.AnalysisMainResponse;
import com.heygongc.notification.domain.entity.Notification;
import com.heygongc.user.domain.entity.User;
import com.heygongc.video.domain.entity.Video;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.AbstractMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(name = "Analysis API", description = "분석 API")
@RestController
@RequestMapping("/v1/analysis")
public class AnalysisController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AnalysisService analysisService;

    public AnalysisController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @GetMapping
    @Operation(
            summary = "분석 정보 조회",
            description = "[분석 > 메인] 하루 요약과 오늘 생성된 영상 1개를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AnalysisMainResponse.class)))
            }
    )
    public ResponseEntity<AnalysisMainResponse> getAnalysisMain(
            @Parameter(description = "조회일자", required = true, in = ParameterIn.QUERY) @RequestParam(name = "requestAt") String requestAt,
            @Parameter(hidden = true) User user
    ) throws ParseException {

        List<Notification> notifications = analysisService.getNotifications(requestAt, user);
        String retunrMsg = "오늘 소리가 %d번 감지되었습니다.";
        List<AnalysisMainResponse.Notifications> responseNotifications = notifications.stream()
                .collect(Collectors.groupingBy(
                        n -> new AbstractMap.SimpleEntry<>(
                                n.getDevice().getDeviceId(),
                                n.getDevice().getDeviceName()),
                        Collectors.counting()))
                .entrySet().stream()
                .map(e -> new AnalysisMainResponse.Notifications(
                        e.getKey().getKey(),
                        e.getKey().getValue(),
                        String.format(retunrMsg, e.getValue())))
                .toList();

        responseNotifications.forEach(
                n -> logger.debug(n.toString())
        );

        Optional<Video> video = analysisService.getVideo(requestAt, user);
        String videoUrl = video.map(Video::getUrl).orElse(null);

        return ResponseEntity.ok()
                .body(
                        new AnalysisMainResponse(
                                responseNotifications,
                                videoUrl
                        )
                );
    }

    @GetMapping("/detail")
    @Operation(
            summary = "분석 상세 조회",
            description = "[분석 > 메인 > 하루 요약 상세] 기기의 하루 총 알림횟수와 시간별 알림횟수를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AnalysisDetailResponse.class)))
            }
    )
    public ResponseEntity<AnalysisDetailResponse> getAnalysisDetail(
            @Parameter(description = "조회일자", required = true, in = ParameterIn.QUERY) @RequestParam(name = "requestAt") String requestAt,
            @Parameter(description = "기기 아이디", required = true, in = ParameterIn.QUERY) @RequestParam(name = "deviceId") String deviceId,
            @Parameter(hidden = true) User user
    ) {
        AnalysisDetailResponse response = analysisService.getAnalysisDetail(requestAt, deviceId, user);
        return ResponseEntity.ok()
                .body(
                        response
                );
    }

}
