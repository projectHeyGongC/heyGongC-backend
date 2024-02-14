package com.heygongc.device.domain;

import com.heygongc.device.application.DeviceSensitivityEnum;
import com.heygongc.global.config.BaseTimeEntity;
import com.heygongc.notification.domain.Notification;
import com.heygongc.user.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@Table(name = "device")
public class Device extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "device_seq")
    private Long deviceSeq;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "sound_mode", nullable = false)
    private boolean soundMode;

    @Column(name = "sensitivity", nullable = false)
    @Enumerated(EnumType.STRING)
    private DeviceSensitivityEnum sensitivity;

    @Column(name = "sound_active", nullable = false)
    private boolean soundActive;

    @Column(name = "stream_active", nullable = false)
    private boolean streamActive;

    @Column(name = "front_camera", nullable = false)
    private boolean frontCamera;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User user;

    @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Notification> notifications = new ArrayList<>();


}
