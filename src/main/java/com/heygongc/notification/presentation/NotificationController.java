package com.heygongc.notification.presentation;

import com.heygongc.notification.application.NotificationService;
import com.heygongc.notification.domain.entity.Notification;
import com.heygongc.notification.presentation.request.AddNotificationRequest;
import com.heygongc.notification.presentation.response.NotificationResponse;
import com.heygongc.user.domain.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Tag(name = "Notification API", description = "알림 API")
@RestController
@RequestMapping("/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    @Operation(
            summary = "알림 목록 조회",
            description = "[뷰어 > 모니터링 > 알림] 사용자의 알림 목록을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = NotificationResponse.class))))
            }
    )
    public ResponseEntity<List<NotificationResponse>> getAllNotifications(@Parameter(hidden = true) User user) {
        List<Notification> notifications = notificationService.getAllNotifications(user.getSeq());

        String retunrMsg = "%s에서 소리가 감지되었습니다.";
        List<NotificationResponse> response = notifications.stream()
                .map(notification -> new NotificationResponse(
                        String.format(retunrMsg, notification.getDevice().getDeviceName()),
                        notification.getCreated_at())
                )
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/summary")
    @Operation(
            summary = "알림 요약 조회",
            description = "[뷰어 > 분석 > 하루 요약] 사용자의 하루 알림 요약을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class)))
            }
    )
    public ResponseEntity<List<NotificationResponse>> getSummaryNotifications(@Parameter(hidden = true) User user) {
        // TODO: 요약을 어떻게 하면 좋을까 ..?
        return ResponseEntity.ok().body(null);
    }

    @PostMapping
    @Operation(
            summary = "알림 추가",
            description = "[카메라앱] 발생한 알림을 추가합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotificationResponse.class)))
            }
    )
    public ResponseEntity<Void> addNotification(@Parameter(hidden = true) User user,
                                                @Parameter(name = "AddNotificationRequest", description = "알림 추가 정보", required = true) @RequestBody AddNotificationRequest request) {
        request.validate();
        notificationService.addNotification(user, request);
        return ResponseEntity.ok().build();
    }
}
