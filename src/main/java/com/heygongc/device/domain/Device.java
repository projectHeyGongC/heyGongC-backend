package com.heygongc.device.domain;

import com.heygongc.device.application.DeviceSensitivityEnum;
import com.heygongc.global.config.BaseTimeEntity;
import com.heygongc.notification.domain.Notification;
import com.heygongc.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "device")
public class Device extends BaseTimeEntity {

    @Id
    @Column(name = "device_seq")
    private Long deviceSeq;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean soundMode;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DeviceSensitivityEnum sensitivity;

    @Column(nullable = false)
    private boolean soundActive;

    @Column(nullable = false)
    private boolean streamActive;

    @Column(nullable = false)
    private boolean frontCamera;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User user;

    @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications = new ArrayList<>();


}
