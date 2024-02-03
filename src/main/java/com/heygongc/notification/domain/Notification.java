package com.heygongc.notification.domain;

import com.heygongc.device.application.DeviceSensitivityEnum;
import com.heygongc.device.domain.Device;
import com.heygongc.notification.application.NotificationTypeEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import com.heygongc.global.config.BaseTimeEntity;
import com.heygongc.user.domain.User;
import jakarta.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notification")
public class Notification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_seq")
    private Long eventSeq;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean readStatus;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationTypeEnum typeEnum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_seq")
    private Device device;

}
