package com.heygongc.notification.domain.entity;

import com.heygongc.device.domain.entity.Device;
import com.heygongc.global.config.BaseTimeEntity;
import com.heygongc.notification.domain.type.NotificationType;
import com.heygongc.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access=PROTECTED) // No default constructor for entity 오류 해결을 위해 필요
@AllArgsConstructor
@DynamicUpdate
@Table(name = "notification")
public class Notification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "noti_seq")
    private Long notiSeq;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Column(name = "notification_at", nullable = false)
    @Builder.Default
    private LocalDateTime notificationAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_seq")
    private Device device;

    @Builder(builderMethodName = "createNotification")
    public Notification(NotificationType type, User user, Device device) {
        this.type = type;
        this.user = user;
        this.device = device;
        // TODO: 이 부분 저장이 되지 않음. 왜 그럴까?
//        this.notificationAt = LocalDateTime.now();
    }
}
