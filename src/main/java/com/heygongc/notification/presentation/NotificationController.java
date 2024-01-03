package com.heygongc.notification.presentation;

import com.heygongc.notification.application.NotificationService;
import com.heygongc.notification.application.NotificationTypeEnum;
import com.heygongc.notification.domain.Notification;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications(@RequestParam String type, HttpServletRequest request) {
        Long userSeq = Long.valueOf((String) request.getAttribute("userSeq"));

        List<Notification> notifications = notificationService.getAllNotifications(userSeq, type);

        return ResponseEntity.ok().body(notifications);
    }

    @PostMapping
    public ResponseEntity<Notification> addNotification(
            @RequestParam Long userSeq,
            @RequestParam Long deviceSeq,
            @RequestParam NotificationTypeEnum type,
            @RequestParam String content) {
        Notification notification = notificationService.addNotification(userSeq, deviceSeq, type, content);
        return ResponseEntity.ok().body(notification);
    }

    @PutMapping("/id")
    public ResponseEntity<Boolean> updateReadStatus(@PathVariable(name = "id") Long eventSeq, HttpServletRequest request){
        Long userSeq = Long.valueOf((String) request.getAttribute("userSeq"));

        Boolean result = notificationService.updateReadStatus(eventSeq, userSeq);

        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteOldNotifications(HttpServletRequest request){
        Long userSeq = Long.valueOf((String) request.getAttribute("userSeq"));
        Boolean result = notificationService.deleteOldNotifications(userSeq);

        return ResponseEntity.ok().body(result);

    }




}
