package com.heygongc.notification.presentation;

import com.heygongc.device.domain.Device;
import com.heygongc.global.argumentresolver.LoginUser;
import com.heygongc.global.common.response.ListResponse;
import com.heygongc.notification.application.NotificationService;
import com.heygongc.notification.application.NotificationTypeEnum;
import com.heygongc.notification.domain.Notification;
import com.heygongc.user.domain.User;
import io.swagger.v3.oas.annotations.Parameter;
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

    @GetMapping
    public ResponseEntity<ListResponse<Notification>> getAllNotifications(@RequestParam String type, @Parameter(hidden = true) @LoginUser User user) {
        Long userSeq = user.getSeq();
        List<Notification> notifications = notificationService.getAllNotifications(userSeq, type);

        ListResponse<Notification> notificationListResponse = new ListResponse<Notification>(
                notifications
        );


        return ResponseEntity.ok().body(notificationListResponse);
    }

    @PostMapping
    public ResponseEntity<Notification> addNotification(
            @Parameter(hidden = true) @LoginUser User user,
            @RequestParam Long deviceSeq,
            @RequestParam NotificationTypeEnum type,
            @RequestParam String content) {
        Long userSeq = user.getSeq();
        Notification notification = notificationService.addNotification(userSeq, deviceSeq, type, content);
        return ResponseEntity.ok().body(notification);
    }

    @PutMapping("/id")
    public ResponseEntity<Boolean> updateReadStatus(@PathVariable(name = "id") Long eventSeq, @Parameter(hidden = true) @LoginUser User user){
        Long userSeq = user.getSeq();

        Boolean result = notificationService.updateReadStatus(eventSeq, userSeq);

        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteOldNotifications(@Parameter(hidden = true) @LoginUser User user){
        Long userSeq = user.getSeq();
        Boolean result = notificationService.deleteOldNotifications(userSeq);

        return ResponseEntity.ok().body(result);

    }




}
