package com.heygongc.notification.presentation;

import com.heygongc.device.domain.Device;
import com.heygongc.device.presentation.response.DeviceResponse;
import com.heygongc.global.argumentresolver.LoginUser;
import com.heygongc.global.common.response.ListResponse;
import com.heygongc.global.error.ErrorResponse;
import com.heygongc.notification.application.NotificationService;
import com.heygongc.notification.application.NotificationTypeEnum;
import com.heygongc.notification.domain.Notification;
import com.heygongc.notification.presentation.request.NotificationInfoRequest;
import com.heygongc.notification.presentation.response.NotificationResponse;
import com.heygongc.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Notification API", description = "알림 API")
@RestController
@RequestMapping("/v1/notification")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }


    @GetMapping("/{eventSeq}")
    @Operation(
            summary = "알림 선택",
            description = "해당 알림의 정보를 가져옵니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotificationResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Unauthorized Exception", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))

            }
    )
    public ResponseEntity<NotificationResponse> getNotification(
            @Parameter(description = "알림 시퀀스", required = true, in = ParameterIn.PATH) @PathVariable(name = "eventSeq") Long eventSeq,
            @Parameter(hidden = true) @LoginUser User user){
        Notification notification = notificationService.getNotification(eventSeq, user);

        NotificationResponse notificationResponse = new NotificationResponse(
                notification.getEventSeq(),
                notification.getUser().getSeq(),
                notification.getTypeEnum(),
                notification.getContent(),
                notification.isReadStatus()
        );

        return ResponseEntity.ok().body(notificationResponse);

    }

    @GetMapping
    @Operation(
            summary = "알림 목록",
            description = "해당 유저의 모든 알림 목록을 나열합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ListResponse.class))),
            }
    )
    public ResponseEntity<ListResponse<Notification>> getAllNotifications(
            @Parameter(description = "알림 종류", required = true) @RequestParam NotificationTypeEnum type,
            @Parameter(hidden = true) @LoginUser User user) {
        Long userSeq = user.getSeq();
        List<Notification> notifications = notificationService.getAllNotifications(userSeq, type);

        ListResponse<Notification> notificationListResponse = new ListResponse<Notification>(
                notifications
        );


        return ResponseEntity.ok().body(notificationListResponse);
    }

    @PostMapping
    @Operation(
            summary = "알림 추가",
            description = "알림을 추가합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotificationResponse.class))),

            }
    )
    public ResponseEntity<NotificationResponse> addNotification(
            @Parameter(hidden = true) @LoginUser User user,
            @Parameter(description = "알림 시퀀스", required = true) @RequestParam Long deviceSeq,
            @RequestBody(description = "알림 정보")NotificationInfoRequest request) {
        Notification notification = notificationService.addNotification(user, deviceSeq, request);
        NotificationResponse notificationResponse = new NotificationResponse(
                notification.getEventSeq(),
                notification.getUser().getSeq(),
                notification.getTypeEnum(),
                notification.getContent(),
                notification.isReadStatus()
        );
        return ResponseEntity.ok().body(notificationResponse);
    }

    @PutMapping("/{eventSeq}")
    @Operation(
            summary = "알림 읽음 표시",
            description = "읽은 알림은 읽음으로 표시합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotificationResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Unauthorized Exception", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))

            }

    )
    public ResponseEntity<NotificationResponse> updateReadStatus(@PathVariable(name = "eventSeq") Long eventSeq, @Parameter(hidden = true) @LoginUser User user){

        Notification notification = notificationService.updateReadStatus(eventSeq, user);

        NotificationResponse notificationResponse = new NotificationResponse(
                notification.getEventSeq(),
                notification.getUser().getSeq(),
                notification.getTypeEnum(),
                notification.getContent(),
                notification.isReadStatus()
        );
        return ResponseEntity.ok().body(notificationResponse);
    }

    @DeleteMapping
    @Operation(
            summary = "오래된 알림 삭제",
            description = "알림이 30일 이상 지나거나 알림 수가 100개를 넘으면 해당 알림들을 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Unauthorized Exception", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<Boolean> deleteOldNotifications(@Parameter(hidden = true) @LoginUser User user){
        Long userSeq = user.getSeq();
        Boolean result = notificationService.deleteOldNotifications(userSeq);

        return ResponseEntity.ok().body(result);

    }




}
