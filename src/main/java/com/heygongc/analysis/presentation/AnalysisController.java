package com.heygongc.analysis.presentation;

import com.heygongc.analysis.application.AnalysisService;
import com.heygongc.analysis.presentation.response.AnalysisDetailResponse;
import com.heygongc.analysis.presentation.response.AnalysisMainResponse;
import com.heygongc.device.application.device.DeviceService;
import com.heygongc.device.domain.entity.Device;
import com.heygongc.notification.application.NotificationService;
import com.heygongc.notification.domain.entity.Notification;
import com.heygongc.user.domain.entity.User;
import com.heygongc.video.application.VideoService;
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
import java.util.List;
import java.util.Optional;

@Tag(name = "Analysis API", description = "분석 API")
@RestController
@RequestMapping("/v1/analysis")
public class AnalysisController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AnalysisService analysisService;
    private final VideoService videoService;
    private final NotificationService notificationService;
    private final DeviceService deviceService;

    public AnalysisController(AnalysisService analysisService, VideoService videoService, NotificationService notificationService, DeviceService deviceService) {
        this.analysisService = analysisService;
        this.videoService = videoService;
        this.notificationService = notificationService;
        this.deviceService = deviceService;
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
            @Parameter(hidden = true) User user,
            @Parameter(description = "조회일자", required = true, in = ParameterIn.QUERY) @RequestParam(name = "requestAt") String requestAt
    ) throws ParseException {

        List<Notification> notifications = notificationService.getNotifications(user, requestAt);;
        List<AnalysisMainResponse.Notifications> responseNotifications = analysisService.makeAnalysisMainResponse(notifications);
        Optional<Video> video = videoService.getVideo(user, requestAt);
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
            @Parameter(hidden = true) User user,
            @Parameter(description = "기기 아이디", required = true, in = ParameterIn.QUERY) @RequestParam(name = "deviceId") String deviceId,
            @Parameter(description = "조회일자", required = true, in = ParameterIn.QUERY) @RequestParam(name = "requestAt") String requestAt
    ) throws ParseException {
        List<Notification> notifications = notificationService.getNotifications(user, requestAt, deviceId);
        Device device = deviceService.getDevice(deviceId);
        List<AnalysisDetailResponse.Graph> graph = analysisService.makeAnalysisGraph(notifications);

        return ResponseEntity.ok()
                .body(
                        new AnalysisDetailResponse(
                                device.getDeviceId(),
                                device.getDeviceName(),
                                requestAt,
                                (long) notifications.size(),
                                graph
                        )
                );
    }

}
