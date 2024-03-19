package com.heygongc.notification.domain.entity;

import com.heygongc.device.domain.Device;
import com.heygongc.global.config.BaseTimeEntity;
import com.heygongc.notification.domain.type.NotificationType;
import com.heygongc.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

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
    }
}
