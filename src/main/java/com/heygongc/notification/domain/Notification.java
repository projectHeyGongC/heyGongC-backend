package com.heygongc.notification.domain;

import com.heygongc.device.domain.entity.Device;
import com.heygongc.notification.application.NotificationTypeEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import com.heygongc.global.config.BaseTimeEntity;
import com.heygongc.user.domain.entity.User;
import jakarta.persistence.*;
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
    @Column(name = "event_seq")
    private Long eventSeq;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "read_status", nullable = false)
    private boolean readStatus;

    @Column(name = "type_enum", nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationTypeEnum typeEnum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_seq")
    private Device device;

    public void markAsRead() {
        this.readStatus = true;
    }
}
